/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package com.threatseal.elasticsearch.jdbc.driver.sql.jdbc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.threatseal.elasticsearch.jdbc.driver.sql.client.ClientException;
import com.threatseal.elasticsearch.jdbc.driver.sql.client.ClientVersion;
import com.threatseal.elasticsearch.jdbc.driver.proto.ColumnInfo;
import com.threatseal.elasticsearch.jdbc.driver.proto.MainResponse;
import com.threatseal.elasticsearch.jdbc.driver.proto.SqlTypedParamValue;
import com.threatseal.elasticsearch.jdbc.driver.proto.SqlVersion;
import com.threatseal.elasticsearch.jdbc.driver.proto.core.Tuple;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.threatseal.elasticsearch.jdbc.driver.sql.client.StringUtils.EMPTY;
import com.threatseal.elasticsearch.jdbc.driver.transformer.Transformer;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jsqlparser.JSQLParserException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import java.util.logging.Level;

/**
 * JDBC specific HTTP client. Since JDBC is not thread-safe, neither is this
 * class.
 */
class JdbcHttpClient {

    private static final Logger logger = Logger.getLogger(JdbcHttpClient.class.getName());
    private RestHighLevelClient restHighLevelClient;
    private RestClient restClient;
    private final JdbcConnection jdbcConn;
    private final JdbcConfiguration conCfg;
    private InfoResponse serverInfo;
    private boolean checkServer;

    /**
     * The SQLException is the only type of Exception the JDBC API can throw
     * (and that the user expects). If we remove it, we need to make sure no
     * other types of Exceptions (runtime or otherwise) are thrown
     */
    JdbcHttpClient(JdbcConnection jdbcConn) throws SQLException {
        this(jdbcConn, true);
    }

    JdbcHttpClient(JdbcConnection jdbcConn, boolean checkServer) throws SQLException {
        this.jdbcConn = jdbcConn;
        conCfg = jdbcConn.config();
        logger.log(Level.INFO, "jdbc conCfg {0}", conCfg);

        logger.log(Level.INFO, "authPass {0} authUser {1} baseUri {2} connectTimeout {3} connectionString {4}", new Object[]{
            conCfg.authPass(),
            conCfg.authUser(),
            conCfg.baseUri(),
            conCfg.connectTimeout(),
            conCfg.connectionString()
        }
        );
        connect();
    }

    private void connect() throws SQLException {
        logger.log(Level.INFO, "JdbcHttpClient.connect ");
        String[] configurations = conCfg.connectionString().split(":");

        logger.log(Level.INFO, "configurations {0}", configurations);

        restClient = RestClient.builder(new HttpHost(configurations[2].replace("//", ""),
                Integer.parseInt(configurations[3]), "http")
        ).setRequestConfigCallback(
                requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout(Long.valueOf(conCfg.connectTimeout()).intValue())
                        .setSocketTimeout(Long.valueOf(conCfg.connectTimeout()).intValue())
        )
                .build();
        try {
            restHighLevelClient = new RestHighLevelClient(restClient);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception initializing RestHighLevelClient {0}", e.getMessage());
        }
        if (checkServer) {
            this.serverInfo = fetchServerInfo();
            checkServerVersion();
        }
    }

    boolean ping(long timeoutInMs) throws SQLException {
        try {
            return restHighLevelClient.ping();
        } catch (IOException e) {
            throw new SQLException("IOException", e);
        }
    }

    Cursor query(String sql, List<SqlTypedParamValue> params, RequestMeta meta) throws SQLException {
        try {

            int fetch = meta.fetchSize() > 0 ? meta.fetchSize() : conCfg.pageSize();
            /*SqlQueryRequest sqlRequest = new SqlQueryRequest(
            sql,
            params,
            conCfg.zoneId(),
            jdbcConn.getCatalog(),
            fetch,
            TimeValue.timeValueMillis(meta.queryTimeoutInMs()),
            TimeValue.timeValueMillis(meta.pageTimeoutInMs()),
            Boolean.FALSE,
            null,
            new RequestInfo(Mode.JDBC, ClientVersion.CURRENT),
            conCfg.fieldMultiValueLeniency(),
            conCfg.indexIncludeFrozen(),
            conCfg.binaryCommunication(),
            conCfg.allowPartialSearchResults()
            );
             */

            logger.log(Level.INFO, "query {0}", sql);
            logger.log(Level.INFO, "params {0} ", params);
            logger.log(Level.INFO, "meta {0}", meta);

            SearchSourceBuilder sourceBuilder = Transformer.transform(sql, params);

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.source(sourceBuilder);

            logger.log(Level.INFO, "search request {0}", searchRequest);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

//            logger.log(Level.INFO, "search response {0}", searchResponse);
            List<List<Object>> rows = new ArrayList<>();

            List<JdbcColumnInfo> searchFields = new ArrayList<>();

            logger.log(Level.INFO, "total hits {0}", searchResponse.getHits().totalHits);
            logger.log(Level.INFO, "hits length {0}", searchResponse.getHits().getHits().length);
            if (searchResponse.getAggregations() != null) {
                logger.log(Level.INFO, "length of aggregations {0}", searchResponse.getAggregations().asList().size());
            }

            if (searchResponse.getAggregations() != null && !searchResponse.getAggregations().asList().isEmpty()) {

                searchFields.add(new JdbcColumnInfo(
                        "key",
                        EsType.TEXT,
                        "table",
                        "catalog",
                        "schema",
                        "Key",
                        255));
                searchFields.add(new JdbcColumnInfo(
                        "docCount",
                        EsType.TEXT,
                        "table",
                        "catalog",
                        "schema",
                        "docCount",
                        255));
                searchFields.add(new JdbcColumnInfo(
                        "keyAsString",
                        EsType.TEXT,
                        "table",
                        "catalog",
                        "schema",
                        "keyAsString",
                        255));

                NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
                for (Aggregation aggregation : searchResponse.getAggregations()) {
                    logger.log(Level.INFO, "aggregation type {0}", aggregation.getType());
                    switch (aggregation.getType()) {
                        case "sterms":
                            Terms terms = (Terms) aggregation;
                            for (Terms.Bucket bucket : terms.getBuckets()) {
                                List<Object> row = new ArrayList<>();
                                row.add(bucket.getKey().toString());
                                row.add(bucket.getDocCount() + "");

                                row.add(bucket.getKeyAsString());
                                rows.add(row);
                            }
                            break;
                        case "date_histogram":
                            Histogram histogram = (Histogram) aggregation;
                            for (Histogram.Bucket bucket : histogram.getBuckets()) {
                                List<Object> row = new ArrayList<>();
                                row.add(bucket.getKey().toString());
                                row.add(bucket.getDocCount() + "");

                                row.add(bucket.getKeyAsString());
                                rows.add(row);
                            }
                            break;
                        default:
                            logger.log(Level.SEVERE, "aggregation type not evaluated {0}", aggregation.getType());
                            break;
                    }
                }

            } else if (searchResponse.getHits().getHits().length > 0) {

                Set<String> fieldSet = new HashSet<>();

                for (SearchHit hit : searchResponse.getHits().getHits()) {

                    fieldSet.addAll(hit.getSource().keySet());
                    fieldSet.addAll(hit.getFields().keySet());
                }

                for (SearchHit hit : searchResponse.getHits().getHits()) {

                    List<Object> row = new ArrayList<>();

                    for (String fieldName : fieldSet) {
                        if (hit.getSource().keySet().contains(fieldName)) {
                            if (hit.getSource().get(fieldName) == null) {
                                row.add("");
                            } else {
                                row.add(hit.getSource().get(fieldName));
                            }
                        } else if (hit.getFields() != null && hit.getFields().containsKey(fieldName)) {
                            String value = "";
                            for (Object object : hit.getField(fieldName).getValues()) {
                                value += (String) object + ",";
                            }
                            row.add(value);
                        } else {
                            row.add("");
                        }
                    }

                    rows.add(row);

                }

                for (String field : fieldSet) {
                    searchFields.add(new JdbcColumnInfo(
                            field,
                            EsType.TEXT,
                            "table",
                            "catalog",
                            "schema",
                            field,
                            255)
                    );
                }
            }

            logger.log(Level.INFO, "fields {0}", searchFields);
            Logger.getLogger(JdbcHttpClient.class.getName()).log(Level.FINER, "fields", searchFields);

//            logger.log(Level.INFO, "rows {0}", rows);
            return new DefaultCursor(this, "cursor", searchFields, rows, meta, Collections.EMPTY_LIST);

        } catch (IOException ex) {
            Logger.getLogger(JdbcHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("IOException ", ex);
        } catch (JSQLParserException ex) {
            Logger.getLogger(JdbcHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("JSQLParserException ", ex);
        }
    }

    /**
     * Read the next page of results and returning the scroll id to use to fetch
     * the next page.
     */
    Tuple<String, List<List<Object>>> nextPage(String cursor, RequestMeta meta) throws SQLException {
        try {
            logger.log(Level.INFO, "JdbcHttpClient.nextPage");
            /*SqlQueryRequest sqlRequest = new SqlQueryRequest(
            cursor,
            TimeValue.timeValueMillis(meta.queryTimeoutInMs()),
            TimeValue.timeValueMillis(meta.pageTimeoutInMs()),
            new RequestInfo(Mode.JDBC),
            conCfg.binaryCommunication(),
            conCfg.allowPartialSearchResults()
            );
             */
            SearchResponse response = restHighLevelClient.search(new SearchRequest()); //.response();
            return new Tuple<>("", Collections.EMPTY_LIST);
            //return new Tuple<>(response.cursor(), response.rows());

        } catch (IOException ex) {
            Logger.getLogger(JdbcHttpClient.class
                    .getName()).log(Level.SEVERE, "IOException", ex);
            throw new SQLException("IOException ", ex);
        }
    }

    boolean queryClose(String cursor) throws SQLException {
        try {
            restClient.close();
            return true;
            //return restHighLevelClient.queryClose(cursor, Mode.JDBC);

        } catch (IOException ex) {
            Logger.getLogger(JdbcHttpClient.class
                    .getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("IOException ", ex);
        }
    }

    InfoResponse serverInfo() throws SQLException {
        if (serverInfo == null) {
            serverInfo = fetchServerInfo();
        }
        return serverInfo;
    }

    private InfoResponse fetchServerInfo() throws SQLException {
        try {
//            MainResponse mainResponse = restHighLevelClient.serverInfo();
            MainResponse mainResponse = new MainResponse("local", "5.6.0", "metron", "uuid");
            SqlVersion version = SqlVersion.fromString(mainResponse.getVersion());
            return new InfoResponse(mainResponse.getClusterName(), version);
        } catch (ClientException ex) {
            throw new SQLException(ex);
        }
    }

    private void checkServerVersion() throws SQLException {
        if (ClientVersion.isServerCompatible(serverInfo.version) == false) {
            throw new SQLException(
                    "This version of the JDBC driver is only compatible with Elasticsearch version "
                    + ClientVersion.CURRENT.majorMinorToString()
                    + " or newer; attempting to connect to a server version "
                    + serverInfo.version.toString()
            );
        }
    }

    /**
     * Converts REST column metadata into JDBC column metadata
     */
    private List<JdbcColumnInfo> toJdbcColumnInfo(List<ColumnInfo> columns) throws SQLException {
        List<JdbcColumnInfo> cols = new ArrayList<>(columns.size());
        for (ColumnInfo info : columns) {
            cols.add(new JdbcColumnInfo(info.name(), TypeUtils.of(info.esType()), EMPTY, EMPTY, EMPTY, EMPTY, info.displaySize()));
        }
        return cols;
    }
}
