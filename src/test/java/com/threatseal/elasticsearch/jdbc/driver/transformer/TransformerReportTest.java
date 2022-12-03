/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jsqlparser.JSQLParserException;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timothy Wachiuri
 */
public class TransformerReportTest {

    public TransformerReportTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void monitoredAssetsTest() {

        //TODO: customer id is a term
        //TODO: put format on timestamp
        try {
            String sql = "SELECT * "
                    + "FROM someTable "
                    + "WHERE `CustomerID.keyword`='enovise' "
                    + "AND timestamp BETWEEN '2022-09-01 00:00:00' "
                    + "AND '2022-10-17 23:59:59' "
                    + "GROUP BY `Hostname.keyword` "
                    + "LIMIT 20";

            String expectedResponse = "{"
                    + "  \"query\" : {"
                    + "    \"bool\" : {"
                    + "      \"must\" : ["
                    + "        {"
                    + "          \"term\" : {"
                    + "            \"CustomerID.keyword\" : {"
                    + "              \"value\" : \"enovise\","
                    + "              \"boost\" : 1.0"
                    + "            }"
                    + "          }"
                    + "        },"
                    + "        {"
                    + "          \"range\" : {"
                    + "            \"timestamp\" : {"
                    + "              \"from\" : \"2022-09-01 00:00:00\","
                    + "              \"to\" : \"2022-10-17 23:59:59\","
                    + "              \"format\" : \"yyyy-MM-dd HH:mm:ss\","
                    + "              \"size\" : 1,"
                    + "              \"include_lower\" : true,"
                    + "              \"include_upper\" : true,"
                    + "              \"format\" : \"yyyy-MM-dd HH:mm:ss\","
                    + "              \"boost\" : 1.0"
                    + "            }"
                    + "          }"
                    + "        }"
                    + "      ],"
                    + "      \"disable_coord\" : false,"
                    + "      \"adjust_pure_negative\" : true,"
                    + "      \"boost\" : 1.0"
                    + "    }"
                    + "  },"
                    + "  \"aggregations\" : {"
                    + "    \"Hostname.keyword\" : {"
                    + "      \"terms\" : {"
                    + "        \"field\" : \"Hostname.keyword\","
                    + "        \"size\" : 20,"
                    + "        \"min_doc_count\" : 1,"
                    + "        \"shard_min_doc_count\" : 0,"
                    + "        \"show_term_doc_count_error\" : false,"
                    + "        \"order\" : ["
                    + "          {"
                    + "            \"_count\" : \"desc\""
                    + "          },"
                    + "          {"
                    + "            \"_term\" : \"asc\""
                    + "          }"
                    + "        ]"
                    + "      }"
                    + "    }"
                    + "  }"
                    + "}";

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, new ArrayList<>());

            System.out.print("response ");
            System.out.println(searchSourceBuilder.toString());
            assertEquals(expectedResponse, searchSourceBuilder.toString());

        } catch (JSQLParserException ex) {
            Logger.getLogger(TransformerReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void logsTrendTest() {
        try {
            String sql = "SELECT * "
                    + "FROM sometable "
                    + "WHERE timestamp BETWEEN '2022-10-02 03:25:00' "
                    + "AND '2022-10-07 09:25:00' "
                    + "AND `CustomerID.keyword`='enovise' "
                    + "GROUP BY timestamp "
                    + "LIMIT 10";

            String expected = "{"
                    + "\"size\" : 10,"
                    + "\"timeout\" : \"240000s\","
                    + "\"query\" : {"
                    + "\"bool\" : {"
                    + "  \"must\" : ["
                    + "    {"
                    + "      \"range\" : {"
                    + "        \"timestamp\" : {"
                    + "          \"from\" : \"2022-10-02 03:25:00\","
                    + "          \"to\" : \"2022-10-07 09:25:00\","
                    + "              \"include_lower\" : true,"
                    + "              \"include_upper\" : true,"
                    + "              \"format\" : \"yyyy-MM-dd HH:mm:ss\","
                    + "              \"boost\" : 1.0"
                    + "            }"
                    + "          }"
                    + "        },"
                    + "        {"
                    + "          \"term\" : {"
                    + "            \"CustomerID.keyword\" : {"
                    + "              \"value\" : \"enovise\","
                    + "              \"boost\" : 1.0"
                    + "            }"
                    + "          }"
                    + "        }"
                    + "      ],"
                    + "      \"disable_coord\" : false,"
                    + "      \"adjust_pure_negative\" : true,"
                    + "      \"boost\" : 1.0"
                    + "    }"
                    + "  },"
                    + "  \"aggregations\" : {"
                    + "    \"timestamp\" : {"
                    + "      \"date_histogram\" : {"
                    + "        \"field\" : \"timestamp\","
                    + "        \"format\" : \"yyyy-MM-dd HH:mm:ss\","
                    + "        \"interval\" : \"12h\","
                    + "        \"size\" :10,"
                    + "        \"order\" : {"
                    + "          \"_key\" : \"asc\""
                    + "        },"
                    + "        \"keyed\" : false,"
                    + "        \"min_doc_count\" : 0"
                    + "      }"
                    + "    }"
                    + "  }"
                    + "}";

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, new ArrayList<>());

            assertEquals(expected, searchSourceBuilder.toString());
        } catch (JSQLParserException ex) {
            Logger.getLogger(TransformerReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void topLogHostTest() {

        try {
            String sql = "SELECT * "
                    + "FROM someTable "
                    + "WHERE `CustomerID.keyword`='enovise' "
                    + "AND timestamp BETWEEN '2022-09-01 00:00:00' "
                    + "AND '2022-10-17 23:59:59' "
                    + "GROUP BY `Hostname.keyword` "
                    + "ORDER BY `Hostname.keyword` DESC "
                    + "LIMIT 1";

            String expectedResponse = "{"
                    + "  \"query\" : {"
                    + "    \"size\":1,"
                    + "    \"bool\" : {"
                    + "      \"must\" : ["
                    + "        {"
                    + "          \"match\" : {"
                    + "            \"CustomerID.keyword\" : {"
                    + "              \"value\" : \"enovise\","
                    + "              \"boost\" : 1.0"
                    + "            }"
                    + "          }"
                    + "        },"
                    + "        {"
                    + "          \"range\" : {"
                    + "            \"timestamp\" : {"
                    + "              \"from\" : \"2022-09-01 00:00:00\","
                    + "              \"to\" : \"2022-10-17 23:59:59\","
                    + "              \"include_lower\" : true,"
                    + "              \"include_upper\" : true,"
                    + "              \"format\" : \"yyyy-MM-dd HH:mm:ss\","
                    + "              \"boost\" : 1.0"
                    + "            }"
                    + "          }"
                    + "        }"
                    + "      ],"
                    + "      \"disable_coord\" : false,"
                    + "      \"adjust_pure_negative\" : true,"
                    + "      \"boost\" : 1.0"
                    + "    }"
                    + "  },"
                    + "  \"aggregations\" : {"
                    + "    \"Hostname.keyword\" : {"
                    + "      \"terms\" : {"
                    + "        \"field\" : \"Hostname.keyword\","
                    + "        \"size\" : 10,"
                    + "        \"min_doc_count\" : 1,"
                    + "        \"shard_min_doc_count\" : 0,"
                    + "        \"show_term_doc_count_error\" : false,"
                    + "        \"order\" : ["
                    + "          {"
                    + "            \"_count\" : \"desc\""
                    + "          },"
                    + "          {"
                    + "            \"_term\" : \"asc\""
                    + "          }"
                    + "        ]"
                    + "      }"
                    + "    }"
                    + "  }"
                    + "}";

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, new ArrayList<>());

            System.out.print("response ");
            System.out.println(searchSourceBuilder.toString());
            assertEquals(expectedResponse, searchSourceBuilder.toString());
        } catch (JSQLParserException ex) {
            Logger.getLogger(TransformerReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void logVolumeTest() {
        try {
            String sql = "SELECT count(*) "
                    + "FROM sometable "
                    + "LIMIT 0";
            String expectedEsQuery = "{\n"
                    + "  \"size\" : 0,\n"
                    + "  \"query\" : {\n"
                    + "    \"match_all\" : {\n"
                    + "      \"boost\" : 1.0\n"
                    + "    }\n"
                    + "  }\n"
                    + "}";
            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, new ArrayList<>());

            System.out.print("response ");
            System.out.println(searchSourceBuilder.toString());
            assertEquals(expectedEsQuery, searchSourceBuilder.toString());
        } catch (JSQLParserException ex) {
            Logger.getLogger(TransformerReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void failedLoginByUsernameCount() {

        try {
            String sql = "SELECT *\n"
                    + "FROM sometable\n"
                    + "WHERE "
                    + "("
                    + "`source:type`='windows-events'\n"
                    + "AND EventID=4625\n"
                    + "AND `CustomerID.keyword`='enovise'\n"
                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-10-17 23:59:59'\n"
                    + ")\n"
                    + "OR\n"
                    + "(\n"
                    + "`source:type`='linux-events'\n"
                    + "AND Message like 'Failed password%'\n"
                    + "AND `CustomerID.keyword`='enovise'\n"
                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-10-17 23:59:59'\n"
                    + ") "
                    + "GROUP BY `TargetUserName.keyword`,`Message.username`";

            String expectedEsQuery = "{\n"
                    + "  \"query\": {\n"
                    + "    \"bool\": {\n"
                    + "      \"must\": [\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"source:type\": {\n"
                    + "              \"value\": \"windows-events\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"EventID\": {\n"
                    + "              \"value\": 4625\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"CustomerID.keyword\": {\n"
                    + "              \"value\": \"enovise\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"range\": {\n"
                    + "            \"timestamp\": {\n"
                    + "              \"gte\": \"2022-09-01 00:00:00\",\n"
                    + "              \"lte\": \"2022-10-17 23:59:59\",\n"
                    + "              \"format\": \"yyyy-MM-dd HH:mm:ss\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        }\n"
                    + "      ]\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"aggregations\": {\n"
                    + "    \"Username\": {\n"
                    + "      \"terms\": {\n"
                    + "        \"field\": \"TargetUserName.keyword\",\n"
                    + "        \"size\": 10\n"
                    + "      }\n"
                    + "    }\n"
                    + "  }\n"
                    + "}";

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, new ArrayList<>());

            System.out.print("response ");
            System.out.println(searchSourceBuilder.toString());
            assertEquals(expectedEsQuery, searchSourceBuilder.toString());
        } catch (JSQLParserException ex) {
            Logger.getLogger(TransformerReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void successfulLoginBySourceUsernameCount() {
        try {
            String sql = "SELECT *\n"
                    + "FROM sometable\n"
                    + "WHERE "
                    + "("
                    + "`source:type`='windows-events'\n"
                    + "AND EventID=4624\n"
                    + "AND `CustomerID.keyword`='enovise'\n"
                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-10-17 23:59:59'\n"
                    + ")\n"
                    + "OR\n"
                    + "(\n"
                    + "`source:type`='linux-events'\n"
                    + "AND Message like 'Accepted%'\n"
                    + "AND `CustomerID.keyword`='enovise'\n"
                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-10-17 23:59:59'\n"
                    + ") "
                    + "GROUP BY `TargetUserName.keyword`,`Message.username`";

            String expectedEsQuery = "{\n"
                    + "  \"query\": {\n"
                    + "    \"bool\": {\n"
                    + "      \"must\": [\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"source:type\": {\n"
                    + "              \"value\": \"windows-events\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"EventID\": {\n"
                    + "              \"value\": 4625\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"CustomerID.keyword\": {\n"
                    + "              \"value\": \"enovise\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"range\": {\n"
                    + "            \"timestamp\": {\n"
                    + "              \"gte\": \"2022-09-01 00:00:00\",\n"
                    + "              \"lte\": \"2022-10-17 23:59:59\",\n"
                    + "              \"format\": \"yyyy-MM-dd HH:mm:ss\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        }\n"
                    + "      ]\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"aggregations\": {\n"
                    + "    \"Username\": {\n"
                    + "      \"terms\": {\n"
                    + "        \"field\": \"TargetUserName.keyword\",\n"
                    + "        \"size\": 10\n"
                    + "      }\n"
                    + "    },\n"
                    + "    {\n"
                    + "    \"UsernameLinux\": {\n"
                    + "      \"terms\": {\n"
                    + "        \"field\": \"TargetUserName.keyword\",\n"
                    + "        \"size\": 10\n"
                    + "      }\n"
                    + "    }\n"
                    + "  }\n"
                    + "}";

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, new ArrayList<>());

            System.out.print("response ");
            System.out.println(searchSourceBuilder.toString());
            assertEquals(expectedEsQuery, searchSourceBuilder.toString());
        } catch (JSQLParserException ex) {
            Logger.getLogger(TransformerReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void successfulLoginBySourceIPCount() {
        try {
            String sql = "SELECT *\n"
                    + "FROM sometable\n"
                    + "WHERE "
                    + "("
                    + "`source:type`='windows-events'\n"
                    + "AND EventID=4624\n"
                    + "AND `CustomerID.keyword`='enovise'\n"
                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-10-17 23:59:59'\n"
                    + ")\n"
                    + "OR\n"
                    + "(\n"
                    + "`source:type`='linux-events'\n"
                    + "AND Message like 'Accepted password%'\n"
                    + "AND `CustomerID.keyword`='enovise'\n"
                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-10-17 23:59:59'\n"
                    + ") "
                    + "GROUP BY `ip_src_addr.keyword` ,Message.ipaddress";

            String expectedEsQuery = "{\n"
                    + "  \"query\": {\n"
                    + "    \"bool\": {\n"
                    + "      \"must\": [\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"source:type\": {\n"
                    + "              \"value\": \"windows-events\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"EventID\": {\n"
                    + "              \"value\": 4625\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"CustomerID.keyword\": {\n"
                    + "              \"value\": \"enovise\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"range\": {\n"
                    + "            \"timestamp\": {\n"
                    + "              \"gte\": \"2022-09-01 00:00:00\",\n"
                    + "              \"lte\": \"2022-10-17 23:59:59\",\n"
                    + "              \"format\": \"yyyy-MM-dd HH:mm:ss\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        }\n"
                    + "      ]\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"aggregations\": {\n"
                    + "    \"Username\": {\n"
                    + "      \"terms\": {\n"
                    + "        \"field\": \"TargetUserName.keyword\",\n"
                    + "        \"size\": 10\n"
                    + "      }\n"
                    + "    }\n"
                    + "  }\n"
                    + "}";

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, new ArrayList<>());

            System.out.print("response ");
            System.out.println(searchSourceBuilder.toString());
            assertEquals(expectedEsQuery, searchSourceBuilder.toString());
        } catch (JSQLParserException ex) {
            Logger.getLogger(TransformerReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void newUsersAddedToSystemTest() {
        try {
            String sql = "SELECT *\n"
                    + "FROM sometable\n"
                    + "WHERE "
                    //                    + "("
                    + "`source:type`='windows-events'\n"
                    + "AND EventID=4720\n"
                    + "AND `CustomerID.keyword`='enovise'\n"
                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-11-27 23:59:59'\n" //                    + ")\n"
                    //                    + "OR\n"
                    //                    + "(\n"
                    //                    + "`source:type`='linux-events'\n"
                    //                    + "AND Message like 'new user%'\n"
                    //                    + "AND `CustomerID.keyword`='enovise'\n"
                    //                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-10-17 23:59:59'\n"
                    //                    + ") "
                    ;

            String expectedEsQuery = "{\n"
                    + "  \"query\": {\n"
                    + "    \"bool\": {\n"
                    + "      \"must\": [\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"source:type\": {\n"
                    + "              \"value\": \"windows-events\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"EventID\": {\n"
                    + "              \"value\": 4625\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"CustomerID.keyword\": {\n"
                    + "              \"value\": \"enovise\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"range\": {\n"
                    + "            \"timestamp\": {\n"
                    + "              \"gte\": \"2022-09-01 00:00:00\",\n"
                    + "              \"lte\": \"2022-10-17 23:59:59\",\n"
                    + "              \"format\": \"yyyy-MM-dd HH:mm:ss\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        }\n"
                    + "      ]\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"aggregations\": {\n"
                    + "    \"Username\": {\n"
                    + "      \"terms\": {\n"
                    + "        \"field\": \"TargetUserName.keyword\",\n"
                    + "        \"size\": 10\n"
                    + "      }\n"
                    + "    }\n"
                    + "  }\n"
                    + "}";

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, new ArrayList<>());

            System.out.print("response ");
            System.out.println(searchSourceBuilder.toString());
            assertEquals(expectedEsQuery, searchSourceBuilder.toString());
        } catch (JSQLParserException ex) {
            Logger.getLogger(TransformerReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void usersDeletedFromSystemTest() {
        try {
            String sql = "SELECT *\n"
                    + "FROM sometable\n"
                    + "WHERE "
                    //                    + "("
                    + "`source:type`='windows-events'\n"
                    + "AND EventID=4726\n"
                    + "AND `CustomerID.keyword`='enovise'\n"
                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-10-17 23:59:59'\n" //                    + ")\n"
                    //                    + "OR\n"
                    //                    + "(\n"
                    //                    + "`source:type`='linux-events'\n"
                    //                    + "AND Message like 'delete user%'\n"
                    //                    + "AND `CustomerID.keyword`='enovise'\n"
                    //                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-10-17 23:59:59'\n"
                    //                    + ") "
                    ;

            String expectedEsQuery = "{\n"
                    + "  \"query\": {\n"
                    + "    \"bool\": {\n"
                    + "      \"must\": [\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"source:type\": {\n"
                    + "              \"value\": \"windows-events\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"EventID\": {\n"
                    + "              \"value\": 4625\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"CustomerID.keyword\": {\n"
                    + "              \"value\": \"enovise\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"range\": {\n"
                    + "            \"timestamp\": {\n"
                    + "              \"gte\": \"2022-09-01 00:00:00\",\n"
                    + "              \"lte\": \"2022-10-17 23:59:59\",\n"
                    + "              \"format\": \"yyyy-MM-dd HH:mm:ss\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        }\n"
                    + "      ]\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"aggregations\": {\n"
                    + "    \"Username\": {\n"
                    + "      \"terms\": {\n"
                    + "        \"field\": \"TargetUserName.keyword\",\n"
                    + "        \"size\": 10\n"
                    + "      }\n"
                    + "    }\n"
                    + "  }\n"
                    + "}";

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, new ArrayList<>());

            System.out.print("response ");
            System.out.println(searchSourceBuilder.toString());
            assertEquals(expectedEsQuery, searchSourceBuilder.toString());
        } catch (JSQLParserException ex) {
            Logger.getLogger(TransformerReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void userAddedToAdminGroupTest() {
        try {
            String sql = "SELECT *\n"
                    + "FROM sometable\n"
                    + "WHERE "
                    //                    + "("
                    + "`source:type`='windows-events'\n"
                    + "AND EventID=4720\n"
                    + "AND PrimaryGroupId!=513\n"
                    + "AND `CustomerID.keyword`='enovise'\n"
                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-11-27 23:59:59'\n" //                    + ")\n"
                    //                    + "OR\n"
                    //                    + "(\n"
                    //                    + "`source:type`='linux-events'\n"
                    //                    + "AND Message like 'delete user%'\n"
                    //                    + "AND `CustomerID.keyword`='enovise'\n"
                    //                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-10-17 23:59:59'\n"
                    //                    + ") "
                    ;

            String expectedEsQuery = "{\n"
                    + "  \"query\": {\n"
                    + "    \"bool\": {\n"
                    + "      \"must\": [\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"source:type\": {\n"
                    + "              \"value\": \"windows-events\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"EventID\": {\n"
                    + "              \"value\": 4625\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"CustomerID.keyword\": {\n"
                    + "              \"value\": \"enovise\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"range\": {\n"
                    + "            \"timestamp\": {\n"
                    + "              \"gte\": \"2022-09-01 00:00:00\",\n"
                    + "              \"lte\": \"2022-10-17 23:59:59\",\n"
                    + "              \"format\": \"yyyy-MM-dd HH:mm:ss\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        }\n"
                    + "      ]\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"aggregations\": {\n"
                    + "    \"Username\": {\n"
                    + "      \"terms\": {\n"
                    + "        \"field\": \"TargetUserName.keyword\",\n"
                    + "        \"size\": 10\n"
                    + "      }\n"
                    + "    }\n"
                    + "  }\n"
                    + "}";

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, new ArrayList<>());

            System.out.print("response ");
            System.out.println(searchSourceBuilder.toString());
            assertEquals(expectedEsQuery, searchSourceBuilder.toString());
        } catch (JSQLParserException ex) {
            Logger.getLogger(TransformerReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void passwordChangeTest() {
        try {
            String sql = "SELECT *\n"
                    + "FROM sometable\n"
                    + "WHERE "
                    //                    + "("
                    + "`source:type`='windows-events'\n"
                    + "AND EventID=4724\n"
                    + "AND `CustomerID.keyword`='enovise'\n"
                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-10-17 23:59:59'\n" //                    + ")\n"
                    //                    + "OR\n"
                    //                    + "(\n"
                    //                    + "`source:type`='linux-events'\n"
                    //                    + "AND Message like 'Password changed%'\n"
                    //                    + "AND `CustomerID.keyword`='enovise'\n"
                    //                    + "AND timestamp between '2022-09-01 00:00:00' and '2022-10-17 23:59:59'\n"
                    //                    + ") "
                    ;

            String expectedEsQuery = "{\n"
                    + "  \"query\": {\n"
                    + "    \"bool\": {\n"
                    + "      \"must\": [\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"source:type\": {\n"
                    + "              \"value\": \"windows-events\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"EventID\": {\n"
                    + "              \"value\": 4625\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"term\": {\n"
                    + "            \"CustomerID.keyword\": {\n"
                    + "              \"value\": \"enovise\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        },\n"
                    + "        {\n"
                    + "          \"range\": {\n"
                    + "            \"timestamp\": {\n"
                    + "              \"gte\": \"2022-09-01 00:00:00\",\n"
                    + "              \"lte\": \"2022-10-17 23:59:59\",\n"
                    + "              \"format\": \"yyyy-MM-dd HH:mm:ss\"\n"
                    + "            }\n"
                    + "          }\n"
                    + "        }\n"
                    + "      ]\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"aggregations\": {\n"
                    + "    \"Username\": {\n"
                    + "      \"terms\": {\n"
                    + "        \"field\": \"TargetUserName.keyword\",\n"
                    + "        \"size\": 10\n"
                    + "      }\n"
                    + "    }\n"
                    + "  }\n"
                    + "}";

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, new ArrayList<>());

            System.out.print("response ");
            System.out.println(searchSourceBuilder.toString());
            assertEquals(expectedEsQuery, searchSourceBuilder.toString());
        } catch (JSQLParserException ex) {
            Logger.getLogger(TransformerReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
