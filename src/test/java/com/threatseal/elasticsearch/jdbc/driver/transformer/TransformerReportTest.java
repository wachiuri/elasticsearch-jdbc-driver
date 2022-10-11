/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.transformer;

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

import com.threatseal.elasticsearch.jdbc.driver.transformer.Transformer;

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

        try {
            String sql = "SELECT * "
                    + "FROM someTable "
                    + "WHERE `CusotmerID.keyword`='enovise' "
                    + "AND timestamp BETWEEN '2022-10-04 08:32:13' "
                    + "AND '2022-10-04 14:32:13' "
                    + "GROUP BY `Hostname.keyword` ";

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
                    + "              \"from\" : \"2022-10-04 08:32:13\","
                    + "              \"to\" : \"2022-10-04 14:32:13\","
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

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, List.of());

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
            String sql = "SELECT * FROM sometable "
                    + "WHERE timestamp>='2022-10-02 03:25:00' "
                    + "AND timestamp<='2022-10-07 09:25:00' "
                    + "AND `CustomerID.keyword`='enovise' "
                    + "GROUP BY timestamp "
                    + "LIMIT 0";

            String expected = "{"
                    + "\"size\" : 0,"
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
                    + "        \"offset\" : 0,"
                    + "        \"order\" : {"
                    + "          \"_key\" : \"asc\""
                    + "        },"
                    + "        \"keyed\" : false,"
                    + "        \"min_doc_count\" : 0"
                    + "      }"
                    + "    }"
                    + "  }"
                    + "}";

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, List.of());

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
                    + "WHERE `CusotmerID.keyword`='enovise' "
                    + "AND timestamp BETWEEN '2022-10-04 08:32:13' "
                    + "AND '2022-10-04 14:32:13' "
                    + "GROUP BY `Hostname.keyword` "
                    + "ORDER BY `Hostname.keyword` DESC"
                    + "LIMIT 1";

            String expectedResponse = "{"
                    + "  \"query\" : {"
                    + "    \"size\":1,"
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
                    + "              \"from\" : \"2022-10-04 08:32:13\","
                    + "              \"to\" : \"2022-10-04 14:32:13\","
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

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, List.of());

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
            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, List.of());

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
                    //+ "("
                    + "`source:type`='windows-events'\n"
                    + "AND EventId=4625\n"
                    + "AND `CustomerID.keyword`='enovise'\n"
                    + "AND timestamp between '2022-10-04 08:32:13' and '2022-10-04 14:32:13'\n"
                    /*+ ")\n"
                + "OR\n"
                + "(\n"
                + "`source:type`='linux-events'\n"
                + "AND `SourceName.keyword`='sshd'\n"
                + "AND Message like 'failed password%'\n"
                + "AND `CustomerID.keyword`='enovise'\n"
                + "AND timestamp between '2022-10-04 08:32:13' and '2022-10-04 14:32:13'\n"
                + ") "
                     */
                    + "GROUP BY `TargetUsername.keyword`";

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
                    + "              \"gte\": \"2022-10-04 08:32:13\",\n"
                    + "              \"lte\": \"2022-10-04 14:32:13\",\n"
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

            SearchSourceBuilder searchSourceBuilder = Transformer.transform(sql, List.of());

            System.out.print("response ");
            System.out.println(searchSourceBuilder.toString());
            assertEquals(expectedEsQuery, searchSourceBuilder.toString());
        } catch (JSQLParserException ex) {
            Logger.getLogger(TransformerReportTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
