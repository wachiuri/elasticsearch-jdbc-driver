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

    public static void main(String[] args) {
        try {
            new Wander().map();
            System.out.println("done");
        } catch (UnknownHostException ex) {
            System.err.println("exception " + ex);
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
                .build(), List.of())
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),
                        9300
                ));
    }

    public void map() throws UnknownHostException, JsonProcessingException {
        
        Entry fieldData = Map.entry("fielddata", true);
        Entry textType = Map.entry("type", "text");

        Entry keyWordType = Map.entry("type", "keyword");
        Entry ignoreAbove = Map.entry("ignore_above", 256);

        Map keyword = Map.ofEntries(keyWordType, ignoreAbove);

        Map ipAddress = Map.of("type", "ip", "ignore_malformed", true, "script", new Script(
                ScriptType.INLINE,
                "groovy",
                "def matcher = doc['Message'].value =~ /(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/;"
                + "emit(matcher.find()?matcher.group():null);",
                Map.of()
        ), "on_script_error", "ignore");

        Map fields = Map.of("keyword", keyword, "ipaddress", ipAddress);

        Map message = Map.of("Message", Map.ofEntries(fieldData, textType, Map.entry("fields", fields)));

        Map properties = Map.of("properties", message);

        PutMappingRequestBuilder messagePutMappingRequestBuilder = new PutMappingRequestBuilder(getTransportClient(), PutMappingAction.INSTANCE);

        System.out.println("mapping " + new ObjectMapper().writeValueAsString(properties));
        messagePutMappingRequestBuilder.setSource(properties);

        messagePutMappingRequestBuilder.setIndices("testindex")
                .setType("testtype")
                .setUpdateAllTypes(false);

        System.out.println("messagePutMappingRequestBuilder " + messagePutMappingRequestBuilder);

        
        messagePutMappingRequestBuilder.execute(new ActionListener<PutMappingResponse>() {
            @Override
            public void onResponse(PutMappingResponse response) {
                System.out.println("message PUT mapping response " + response);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("message PUT mapping failure " + e.getMessage());
                e.printStackTrace(System.out);
            }
        });
    }

    private void testSplitString() {

        String message = "Failed password for invalid user xmq from 45.175.18.29 port 48580 ssh2";
        System.out.println(message);
        String substring = message.substring(33);
        System.out.println(substring);
        int indexOfSpace = substring.indexOf(' ');
        System.out.println("indexOfSpace " + indexOfSpace);
        String username = substring.substring(0, indexOfSpace);
        System.out.println(username);

        String message1 = "Failed password for root from 45.175.18.29 port 48580 ssh2";
        System.out.println(message1);
        String substring1 = message1.substring(20);
        System.out.println(substring1);
        int indexOfSpace1 = substring1.indexOf(' ');
        System.out.println("indexOfSpace " + indexOfSpace1);
        String username1 = substring1.substring(0, indexOfSpace1);
        System.out.println(username1);

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
                        Map.of()
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
        System.out.println("source " + searchSourceBuilder);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("linux-events_index_2022.10.01.00");
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = getClient().search(searchRequest);
            System.out.println("searchResponse " + searchResponse);
        } catch (IOException ex) {
            Logger.getLogger(Wander.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
