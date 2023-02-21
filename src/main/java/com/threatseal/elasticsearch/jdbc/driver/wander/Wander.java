/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.wander;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingAction;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 *
 * @author Timothy Wachiuri
 */
public class Wander {
    
    private static final Logger logger = Logger.getLogger(Wander.class.getName());

    public static void main(String[] args) {
        try {
            new Wander().map();
            logger.log(Level.INFO,"done");
        } catch (UnknownHostException ex) {
            logger.log(Level.SEVERE, "exception {0}", ex);
            Logger.getLogger(Wander.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Wander.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public RestHighLevelClient getClient() {
        RestClient restClient = RestClient.builder(new HttpHost("10.4.0.1",
                9200, "http")
        ).setRequestConfigCallback(
                requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout(30000)
                        .setSocketTimeout(30000)
        )
                .build();
        return new RestHighLevelClient(restClient);
    }

    public TransportClient getTransportClient() throws UnknownHostException {
        return new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name", "elasticsearch")
                .build(), new ArrayList<>())
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),
                        9300
                ));
    }

    public void map() throws UnknownHostException, JsonProcessingException {

        Map<String, Object> keyword = new HashMap();

        keyword.put("type", "keyword");
        keyword.put("ignore_above", 256);

        Map<String, Object> ipAddress = new HashMap();
        ipAddress.put("type", "ip");
        ipAddress.put("ignore_malformed", true);
        ipAddress.put("script", new Script(
                ScriptType.INLINE,
                "groovy",
                "def matcher = doc['Message'].value =~ /(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/;"
                + "emit(matcher.find()?matcher.group():null);",
                new HashMap<>()
        ));
        ipAddress.put("on_script_error", "ignore");

        Map<String, Object> fields = new HashMap();

        fields.put("keyword", keyword);
        fields.put("ipaddress", ipAddress);

        Map<String, Object> message = new HashMap();
        Map<String, Object> messageMap = new HashMap<>();

        messageMap.put("fielddata", true);
        messageMap.put("type", "text");

        message.put("Message", messageMap);
        message.put("fields", fields);

        Map<String, Object> properties = new HashMap();
        properties.put("Properties", message);

        PutMappingRequestBuilder messagePutMappingRequestBuilder = new PutMappingRequestBuilder(getTransportClient(), PutMappingAction.INSTANCE);

        logger.log(Level.INFO, "mapping {0}", new ObjectMapper().writeValueAsString(properties));
        messagePutMappingRequestBuilder.setSource(properties);

        messagePutMappingRequestBuilder.setIndices("testindex")
                .setType("testtype")
                .setUpdateAllTypes(false);

        logger.log(Level.INFO, "messagePutMappingRequestBuilder {0}", messagePutMappingRequestBuilder);

        messagePutMappingRequestBuilder.execute(new ActionListener<PutMappingResponse>() {
            @Override
            public void onResponse(PutMappingResponse response) {
                logger.log(Level.INFO, "message PUT mapping response {0}", response);
            }

            @Override
            public void onFailure(Exception e) {
                logger.log(Level.SEVERE, "message PUT mapping failure {0}", e.getMessage());
                //e.printStackTrace(System.out);
            }
        });
    }

    private void testSplitString() {

        String message = "Failed password for invalid user xmq from 45.175.18.29 port 48580 ssh2";
        logger.log(Level.INFO,message);
        String substring = message.substring(33);
        logger.log(Level.INFO,substring);
        int indexOfSpace = substring.indexOf(' ');
        logger.log(Level.INFO, "indexOfSpace {0}", indexOfSpace);
        String username = substring.substring(0, indexOfSpace);
        logger.log(Level.INFO,username);

        String message1 = "Failed password for root from 45.175.18.29 port 48580 ssh2";
        logger.log(Level.INFO,message1);
        String substring1 = message1.substring(20);
        logger.log(Level.INFO,substring1);
        int indexOfSpace1 = substring1.indexOf(' ');
        logger.log(Level.INFO, "indexOfSpace {0}", indexOfSpace1);
        String username1 = substring1.substring(0, indexOfSpace1);
        logger.log(Level.INFO,username1);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("source:type", "linux-events"))
                .must(QueryBuilders.matchPhraseQuery("Message", "Failed password"))
                .must(QueryBuilders.termQuery("CustomerID.keyword", "enovise"));
        searchSourceBuilder.aggregation(AggregationBuilders.terms("terms")
                //                .field("Message")
                .script(new Script(
                        ScriptType.INLINE,
                        "painless",
                        //"return params['_source']['Message'].substring(0,33)",
                        "def message=\"\";"
                        + "def originalMessage=params['_source']['Message'];"
                        + "def length=0;"
                        + "if(originalMessage.substring(0,33).equals("
                        + "\"Failed password for invalid user \")){"
                        + "length=33;"
                        + "}"
                        + "else if (originalMessage.substring(0,20).equals("
                        + "\"Failed password for \")){ "
                        + "length=20;"
                        + "} else{"
                        + "return null;"
                        + "}"
                        //                                + "return length;"
                        + "String newString=originalMessage.substring(length);" //                        + "return newString;"
                        + "int indexOfSpace=newString.indexOf(' ');" //                        + "return indexOfSpace;" 
                        + "return newString.substring(0,indexOfSpace);",
                        new HashMap<>()
                )))
                .query(boolQueryBuilder) //                .size(0)
                ;

        /*
        searchSourceBuilder.scriptField("messageconcat", new Script(
                ScriptType.INLINE, 
                "painless",
                "doc['Message']+\" concat\"",
                Map.of()), true);
         */
        logger.log(Level.INFO, "source {0}", searchSourceBuilder);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("linux-events_index_2022.10.01.00");
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = getClient().search(searchRequest);
            logger.log(Level.INFO, "searchResponse {0}", searchResponse);
        } catch (IOException ex) {
            Logger.getLogger(Wander.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
