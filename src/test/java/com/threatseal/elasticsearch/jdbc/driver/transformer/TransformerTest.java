package com.threatseal.elasticsearch.jdbc.driver.transformer;

import com.threatseal.elasticsearch.jdbc.driver.proto.SqlTypedParamValue;
import java.io.File;
import org.junit.Test;
import java.util.List;
import net.sf.jsqlparser.JSQLParserException;
import java.io.IOException;
import net.sf.jsqlparser.statement.select.OrderByElement;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import static org.junit.Assert.*;

//@Execution(ExecutionMode.CONCURRENT)
public class TransformerTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        //some code

        File file = new File("./queriesAnalysis.csv");
        file.delete();

        File noOfColumns = new File("./noOfColumns.csv");
        noOfColumns.delete();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMultiPartTableNameWithServerNameAndDatabaseNameAndSchemaName() throws Exception {
        assertTrue(Transformer.transform("SELECT columnName "
                + "FROM [server-name\\server-instance].databaseName.schemaName.tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartTableNameWithServerNameAndDatabaseName() throws Exception {
        assertTrue(Transformer.transform("SELECT columnName FROM [server-name\\server-instance].databaseName..tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartTableNameWithServerNameAndSchemaName() throws Exception {
        assertTrue(Transformer.transform("SELECT columnName FROM [server-name\\server-instance]..schemaName.tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartTableNameWithServerProblem() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM LINK_100.htsac.dbo.t_transfer_num a", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartTableNameWithServerName() throws Exception {
        assertTrue(Transformer.transform("SELECT columnName FROM [server-name\\server-instance]...tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartTableNameWithDatabaseNameAndSchemaName() throws Exception {
        assertTrue(Transformer.transform("SELECT columnName FROM databaseName.schemaName.tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartTableNameWithDatabaseName() throws Exception {
        assertTrue(Transformer.transform("SELECT columnName FROM databaseName..tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartTableNameWithSchemaName() throws Exception {
        assertTrue(Transformer.transform("SELECT columnName FROM schemaName.tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartTableNameWithColumnName() throws Exception {
        assertTrue(Transformer.transform("SELECT columnName FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartColumnNameWithDatabaseNameAndSchemaNameAndTableName() throws Exception {
        assertTrue(Transformer.transform("SELECT databaseName.schemaName.tableName.columnName FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartColumnNameWithDatabaseNameAndSchemaName() throws Exception {
        assertTrue(Transformer.transform("SELECT databaseName.schemaName..columnName FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartColumnNameWithDatabaseNameAndTableName() throws Exception {
        assertTrue(Transformer.transform("SELECT databaseName..tableName.columnName FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartColumnNameWithDatabaseName() throws Exception {
        assertTrue(Transformer.transform("SELECT databaseName...columnName FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartColumnNameWithSchemaNameAndTableName() throws Exception {
        assertTrue(Transformer.transform("SELECT schemaName.tableName.columnName FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartColumnNameWithSchemaName() throws Exception {
        assertTrue(Transformer.transform("SELECT schemaName..columnName FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartColumnNameWithTableName() throws Exception {
        assertTrue(Transformer.transform("SELECT tableName.columnName FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartColumnName() throws Exception {
        assertTrue(Transformer.transform("SELECT columnName FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAllColumnsFromTable() throws Exception {
        assertTrue(Transformer.transform("SELECT tableName.* FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSimpleSigns() throws Exception {
        assertTrue(Transformer.transform("SELECT +1, -1 FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSimpleAdditionsAndSubtractionsWithSigns() throws Exception {
        assertTrue(Transformer.transform("SELECT 1 - 1, 1 + 1, -1 - 1, -1 + 1, +1 + 1, +1 - 1 FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOperationsWithSigns() throws Exception {
    }

    @Test
    public void testSignedColumns() throws Exception {
        assertTrue(Transformer.transform("SELECT -columnName, +columnName, +(columnName), -(columnName) FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSigns() throws Exception {
        assertTrue(Transformer.transform("SELECT (-(1)), -(1), (-(columnName)), -(columnName), (-1), -1, (-columnName), -columnName FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimit() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT 3, ?", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT ? OFFSET 3", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 OFFSET ?", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform(" (SELECT * FROM mytable2 WHERE mytable2.col = 9 OFFSET ?) LIMIT 3, 4", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform(" (SELECT * FROM mytable2 WHERE mytable2.col = 9 OFFSET ?) LIMIT 4 OFFSET 3", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform(" (SELECT * FROM mytable3 WHERE mytable4.col = 9 OFFSET ?) LIMIT 4 OFFSET 3", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimit2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT 3, ?", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT ? OFFSET 3", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT NULL OFFSET 3", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT ALL OFFSET 5", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT 0 OFFSET 3", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 OFFSET ?", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform(" (SELECT * FROM mytable2 WHERE mytable2.col = 9 OFFSET ?) LIMIT 3, 4", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform(" (SELECT * FROM mytable2 WHERE mytable2.col = 9 OFFSET ?) LIMIT 4 OFFSET 3", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform(" (SELECT * FROM mytable3 WHERE mytable4.col = 9 OFFSET ?) LIMIT 4 OFFSET 3", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimit3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT ?1, 2", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT 1, ?2", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT ?1, ?2", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT 1, ?", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT ?, ?", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT ?1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimit4() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT :some_name, 2", List.of(new SqlTypedParamValue("Integer", 1))) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT 1, :some_name", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT :name1, :name2", List.of(new SqlTypedParamValue("Integer", 0), new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT ?1, :name1", List.of(new SqlTypedParamValue("Integer", 0), new SqlTypedParamValue("Integer", 1))) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT :name1, ?1", List.of(new SqlTypedParamValue("Integer", 0), new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT :param_name", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimitSqlServer1() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 ORDER BY mytable.id OFFSET 3 ROWS FETCH NEXT 5 ROWS ONLY", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimitSqlServer2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 ORDER BY mytable.id OFFSET 3 ROW FETCH FIRST 5 ROW ONLY", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimitSqlServer3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 ORDER BY mytable.id OFFSET 3 ROWS", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimitSqlServer4() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 ORDER BY mytable.id FETCH NEXT 5 ROWS ONLY", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimitSqlServerJdbcParameters() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 ORDER BY mytable.id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimitPR404() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT ?1", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 LIMIT :param_name", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimitOffsetIssue462() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable LIMIT ?1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimitOffsetIssue462_2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable LIMIT ?1 OFFSET ?2", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimitOffsetKeyWordAsNamedParameter() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable LIMIT :limit", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimitOffsetKeyWordAsNamedParameter2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable LIMIT :limit OFFSET :offset", List.of(new SqlTypedParamValue("Integer", 10), new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTop() throws Exception {
        assertTrue(Transformer.transform("SELECT TOP 3 * FROM mytable WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("select top 5 foo from bar", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTopWithParenthesis() throws Exception {
        assertTrue(Transformer.transform("SELECT TOP (5) PERCENT \"\" + firstColumnName + \"\", \"\" + secondColumnName + \"\" FROM schemaName.tableName alias ORDER BY \"\" + secondColumnName + \"\"  DESC", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTopWithTies() throws Exception {
        assertTrue(Transformer.transform("SELECT TOP (5) PERCENT WITH TIES columnName1, columnName2 FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTopWithJdbcParameter() throws Exception {
        assertTrue(Transformer.transform("SELECT TOP ?1 * FROM mytable WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("select top :name1 foo from bar", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("select top ? foo from bar", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSkip() throws Exception {
        assertTrue(Transformer.transform("SELECT SKIP 5  + firstColumnName + ,  + secondColumnName +  FROM schemaName.tableName alias ORDER BY  + secondColumnName +  DESC", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("String statement2 = SELECT SKIP skipVar c1, c2 FROM t", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFirst() throws Exception {
        assertTrue(Transformer.transform("SELECT FIRST 5  + firstColumnName + ,  + secondColumnName +  FROM schemaName.tableName alias ORDER BY  + secondColumnName +  DESC", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("String statement2 = SELECT FIRST firstVar c1, c2 FROM t", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFirstWithKeywordLimit() throws Exception {
        assertTrue(Transformer.transform("SELECT LIMIT ?  + firstColumnName + ,  + secondColumnName +  FROM schemaName.tableName alias ORDER BY  + secondColumnName +  DESC", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSkipFirst() throws Exception {
        assertTrue(Transformer.transform("SELECT SKIP ?1 FIRST f1 c1, c2 FROM t1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectItems() throws Exception {
        assertTrue(Transformer.transform("SELECT myid AS MYID, mycol, tab.*, schema.tab.*, mytab.mycol2, myschema.mytab.mycol, myschema.mytab.* FROM mytable WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT myid AS MYID, (SELECT MAX(ID) AS myid2 FROM mytable2) AS myalias FROM mytable WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT (myid + myid2) AS MYID FROM mytable WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTimezoneExpression() throws Exception {
        assertTrue(Transformer.transform("SELECT creation_date AT TIME ZONE 'UTC'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTimezoneExpressionWithTwoTransformations() throws Exception {
        assertTrue(Transformer.transform("SELECT DATE(date1 AT TIME ZONE 'UTC' AT TIME ZONE 'australia/sydney') AS another_date", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTimezoneExpressionWithColumnBasedTimezone() throws Exception {
        assertTrue(Transformer.transform("SELECT 1 FROM tbl WHERE col AT TIME ZONE timezone_col < '2021-11-05 00:00:35'::date + INTERVAL '1 day' * 0", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testUnionWithOrderByAndLimitAndNoBrackets() throws Exception {
        assertTrue(Transformer.transform("SELECT id FROM table1 UNION SELECT id FROM table2 ORDER BY id ASC LIMIT 55", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testUnion() throws Exception {
        assertTrue(Transformer.transform(" SELECT * FROM mytable3 WHERE mytable3.col = ? UNION   SELECT * FROM mytable2 LIMIT 3, 4", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform(" SELECT * FROM mytable2 LIMIT 3, 4", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform(" SELECT * FROM mytable3 WHERE mytable3.col = ? UNION   SELECT * FROM mytable2 ORDER BY COL DESC FETCH FIRST 1 ROWS ONLY WITH UR", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testUnion2() throws Exception {
        assertTrue(Transformer.transform(" SELECT * FROM mytable3 WHERE mytable3.col = ? UNION   SELECT * FROM mytable2 LIMIT 3 OFFSET 4", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform(" SELECT * FROM mytable2 LIMIT 3 OFFSET 4", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDistinct() throws Exception {
        assertTrue(Transformer.transform("SELECT DISTINCT ON (myid) myid, mycol FROM mytable WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIsDistinctFrom() throws Exception {
        assertTrue(Transformer.transform("SELECT name FROM tbl WHERE name IS DISTINCT FROM foo", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIsNotDistinctFrom() throws Exception {
        assertTrue(Transformer.transform("SELECT name FROM tbl WHERE name IS NOT DISTINCT FROM foo", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDistinctTop() throws Exception {
        assertTrue(Transformer.transform("SELECT DISTINCT TOP 5 myid, mycol FROM mytable WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDistinctTop2() throws Exception {
        assertTrue(Transformer.transform("SELECT TOP 5 DISTINCT myid, mycol FROM mytable WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDistinctWithFollowingBrackets() throws Exception {
    }

    @Test
    public void testFrom() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable as mytable0, mytable1 alias_tab1, mytable2 as alias_tab2, (SELECT * FROM mytable3) AS mytable4 WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("String statementToString = SELECT * FROM mytable AS mytable0, mytable1 alias_tab1, mytable2 AS alias_tab2, (SELECT * FROM mytable3) AS mytable4 WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testJoin() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab1 LEFT OUTER JOIN tab2 ON tab1.id = tab2.id", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM tab1 LEFT OUTER JOIN tab2 ON tab1.id = tab2.id INNER JOIN tab3", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM tab1 LEFT OUTER JOIN tab2 ON tab1.id = tab2.id JOIN tab3", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM tab1 LEFT OUTER JOIN tab2 ON tab1.id = tab2.id INNER JOIN tab3", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM TA2 LEFT OUTER JOIN O USING (col1, col2) WHERE D.OasSD = 'asdf' AND (kj >= 4 OR l < 'sdf')", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM tab1 INNER JOIN tab2 USING (id, id2)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM tab1 RIGHT OUTER JOIN tab2 USING (id, id2)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM foo AS f LEFT OUTER JOIN (bar AS b RIGHT OUTER JOIN baz AS z ON f.id = z.id) ON f.id = b.id", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM foo AS f, OUTER bar AS b WHERE f.id = b.id", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFunctions() throws Exception {
        assertTrue(Transformer.transform("SELECT MAX(id) AS max FROM mytable WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT substring(id, 2, 3), substring(id from 2 for 3), substring(id from 2), trim(BOTH ' ' from 'foo bar '), trim(LEADING ' ' from 'foo bar '), trim(TRAILING ' ' from 'foo bar '), trim(' ' from 'foo bar '), position('foo' in 'bar'), overlay('foo' placing 'bar' from 1), overlay('foo' placing 'bar' from 1 for 2) FROM my table", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT MAX(id), AVG(pro) AS myavg FROM mytable WHERE mytable.col = 9 GROUP BY pro", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT MAX(a, b, c), COUNT(*), D FROM tab1 GROUP BY D", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT {fn MAX(a, b, c)}, COUNT(*), D FROM tab1 GROUP BY D", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT ab.MAX(a, b, c), cd.COUNT(*), D FROM tab1 GROUP BY D", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testEscapedFunctionsIssue647() throws Exception {
        assertTrue(Transformer.transform("SELECT {fn test(0)} AS COL", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT {fn concat(a, b)} AS COL", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testEscapedFunctionsIssue753() throws Exception {
        assertTrue(Transformer.transform("SELECT fn FROM fn", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNamedParametersPR702() throws Exception {
        assertTrue(Transformer.transform("SELECT substring(id, 2, 3), substring(id from 2 for 3), substring(id from 2), trim(BOTH ' ' from 'foo bar '), trim(LEADING ' ' from 'foo bar '), trim(TRAILING ' ' from 'foo bar '), trim(' ' from 'foo bar '), position('foo' in 'bar'), overlay('foo' placing 'bar' from 1), overlay('foo' placing 'bar' from 1 for 2) FROM my table", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNamedParametersPR702_2() throws Exception {
        assertTrue(Transformer.transform("SELECT substring(id, 2, 3) FROM mytable", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT substring(id from 2 for 3) FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testQuotedCastExpression() throws Exception {
        assertTrue(Transformer.transform("SELECT col FROM test WHERE status = CASE WHEN anothercol = 5 THEN 'pending'::\"enum_test\" END", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWhere() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testGroupBy() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE a > 34 GROUP BY tab1.b", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE a > 34 GROUP BY 2, 3", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testHaving() throws Exception {
        assertTrue(Transformer.transform("SELECT MAX(tab1.b) FROM tab1 WHERE a > 34 GROUP BY tab1.b HAVING MAX(tab1.b) > 56", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT MAX(tab1.b) FROM tab1 WHERE a > 34 HAVING MAX(tab1.b) IN (56, 32, 3, ?)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testExists() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE EXISTS (SELECT * FROM tab2) ", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotExists() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE NOT EXISTS (SELECT * FROM tab2)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotExistsIssue() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM t001 t WHERE NOT EXISTS (SELECT * FROM t002 t1 WHERE t.c1 = t1.c1 AND t.c2 = t1.c2 AND ('241' IN (t1.c3 || t1.c4)))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOrderBy() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE a > 34 GROUP BY tab1.b ORDER BY tab1.a DESC, tab1.b ASC", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("String statementToString = SELECT * FROM tab1 WHERE a > 34 GROUP BY tab1.b ORDER BY tab1.a DESC, tab1.b ASC", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE a > 34 GROUP BY tab1.b ORDER BY tab1.a, 2", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOrderByNullsFirst() throws Exception {
        assertTrue(Transformer.transform("SELECT a FROM tab1 ORDER BY a NULLS FIRST", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOrderByWithComplexExpression() throws Exception {
        assertTrue(Transformer.transform("SELECT col FROM tbl tbl_alias ORDER BY tbl_alias.id = 1 DESC", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTimestamp() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE a > {ts '2004-04-30 04:05:34.56'}", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTime() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE a > {t '04:05:34'}", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testBetweenDate() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE col BETWEEN {d '2015-09-19'} AND {d '2015-09-24'}", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCase() throws Exception {
        assertTrue(Transformer.transform("SELECT a, CASE b WHEN 1 THEN 2 END FROM tab1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT a, (CASE WHEN (a > 2) THEN 3 END) AS b FROM tab1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT a, (CASE WHEN a > 2 THEN 3 ELSE 4 END) AS b FROM tab1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT a, (CASE b WHEN 1 THEN 2 WHEN 3 THEN 4 ELSE 5 END) FROM tab1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT a, (CASE  + WHEN b > 1 THEN 'BBB'  + WHEN a = 3 THEN 'AAA'  + END) FROM tab1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform(" WHERE c = (CASE   WHEN d <> 3 THEN 5   ELSE 10   END)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT a, CASE a  + WHEN 'b' THEN 'BBB'  + WHEN 'a' THEN 'AAA'  + END AS b FROM tab1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT a FROM tab1 WHERE CASE b WHEN 1 THEN 2 WHEN 3 THEN 4 ELSE 5 END > 34", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT a FROM tab1 WHERE CASE b WHEN 1 THEN 2 + 3 ELSE 4 END > 34", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNestedCaseCondition() throws Exception {
        assertTrue(Transformer.transform("SELECT CASE WHEN CASE WHEN 1 THEN 10 ELSE 20 END > 15 THEN 'BBB' END FROM tab1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT (CASE WHEN (CASE a WHEN 1 THEN 10 ELSE 20 END) > 15 THEN 'BBB' END) FROM tab1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue371SimplifiedCase() throws Exception {
        assertTrue(Transformer.transform("SELECT CASE col + 4 WHEN 2 THEN 1 ELSE 0 END", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue371SimplifiedCase2() throws Exception {
        assertTrue(Transformer.transform("SELECT CASE col > 4 WHEN true THEN 1 ELSE 0 END", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue235SimplifiedCase3() throws Exception {
        assertTrue(Transformer.transform("SELECT CASE WHEN (CASE WHEN (CASE WHEN (1) THEN 0 END) THEN 0 END) THEN 0 END FROM a", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue235SimplifiedCase4() throws Exception {
        assertTrue(Transformer.transform("SELECT CASE WHEN (CASE WHEN (CASE WHEN (CASE WHEN (1) THEN 0 END) THEN 0 END) THEN 0 END) THEN 0 END FROM a", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue862CaseWhenConcat() throws Exception {
        assertTrue(Transformer.transform("SELECT c1, CASE c1 || c2 WHEN '091' THEN '2' ELSE '1' END AS c11 FROM T2", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testExpressionsInCaseBeforeWhen() throws Exception {
        assertTrue(Transformer.transform("SELECT a FROM tbl1 LEFT JOIN tbl2 ON CASE tbl1.col1 WHEN tbl1.col1 = 1 THEN tbl1.col2 = tbl2.col2 ELSE tbl1.col3 = tbl2.col3 END", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testExpressionsInIntervalExpression() throws Exception {
        assertTrue(Transformer.transform("SELECT DATE_SUB(mydate, INTERVAL DAY(anotherdate) - 1 DAY) FROM tbl", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testReplaceAsFunction() throws Exception {
        assertTrue(Transformer.transform("SELECT REPLACE(a, 'b', c) FROM tab1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLike() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE a LIKE 'test'", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE a LIKE 'test' ESCAPE 'test2'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotLike() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE a NOT LIKE 'test'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotLikeWithNotBeforeExpression() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE NOT a LIKE 'test'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotLikeIssue775() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mybatisplus WHERE id NOT LIKE ?", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIlike() throws Exception {
        assertTrue(Transformer.transform("SELECT col1 FROM table1 WHERE col1 ILIKE '%hello%'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectOrderHaving() throws Exception {
        assertTrue(Transformer.transform("SELECT units, count(units) AS num FROM currency GROUP BY units HAVING count(units) > 1 ORDER BY num", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDouble() throws Exception {
        assertTrue(Transformer.transform("SELECT 1e2, * FROM mytable WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 1.e2", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 1.2e2", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 2e2", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDouble2() throws Exception {
        assertTrue(Transformer.transform("SELECT 1.e22 FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDouble3() throws Exception {
        assertTrue(Transformer.transform("SELECT 1. FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDouble4() throws Exception {
        assertTrue(Transformer.transform("SELECT 1.2e22 FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWith() throws Exception {
        assertTrue(Transformer.transform(" WHERE THIS_EMP.JOB = 'SALESREP' AND THIS_EMP.WORKDEPT = DINFO.DEPTNO", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWithRecursive() throws Exception {
        assertTrue(Transformer.transform("WITH RECURSIVE t (n) AS ((SELECT 1) UNION ALL (SELECT n + 1 FROM t WHERE n < 100)) SELECT sum(n) FROM t", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectAliasInQuotes() throws Exception {
        assertTrue(Transformer.transform("SELECT mycolumn AS \"My Column Name\" FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectAliasWithoutAs() throws Exception {
        assertTrue(Transformer.transform("SELECT mycolumn \"My Column Name\" FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectJoinWithComma() throws Exception {
        assertTrue(Transformer.transform(" WHERE es.nombre = \"Tamaulipas\" AND cb.the_geom = es.geom", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDeparser() throws Exception {
        assertTrue(Transformer.transform(" WHERE b.BUYERID = a.OWNERID AND b.ITEM = 'Chair'", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT count(DISTINCT f + 4) FROM a", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT count(DISTINCT f, g, h) FROM a", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCount2() throws Exception {
        assertTrue(Transformer.transform("SELECT count(ALL col1 + col2) FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCount3() throws Exception {
        assertTrue(Transformer.transform("SELECT count(UNIQUE col) FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMysqlQuote() throws Exception {
        assertTrue(Transformer.transform(" WHERE b.BUYERID = a.OWNERID AND b.ITEM = 'Chair'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testConcat() throws Exception {
        assertTrue(Transformer.transform("SELECT a || b || c + 4 FROM t", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testConcatProblem2() throws Exception {
        assertTrue(Transformer.transform(" ) || (SPA.GESLACHT)::VARCHAR (1))) AS GESLACHT_TMP FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testConcatProblem2_1() throws Exception {
        assertTrue(Transformer.transform("SELECT TO_CHAR(SPA.AANLEVERPERIODEVOLGNR, 'FM09'::VARCHAR) FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testConcatProblem2_2() throws Exception {
        assertTrue(Transformer.transform("SELECT MAX((SPA.SOORTAANLEVERPERIODE)::VARCHAR (2) || (VARCHAR(SPA.AANLEVERPERIODEJAAR))::VARCHAR (4)) AS GESLACHT_TMP FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testConcatProblem2_3() throws Exception {
        assertTrue(Transformer.transform("SELECT TO_CHAR((10000 - SPA.VERSCHIJNINGSVOLGNR), 'FM0999'::VARCHAR) FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testConcatProblem2_4() throws Exception {
        assertTrue(Transformer.transform("SELECT (SPA.GESLACHT)::VARCHAR (1) FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testConcatProblem2_5() throws Exception {
        assertTrue(Transformer.transform("SELECT max((a || b) || c) FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testConcatProblem2_5_1() throws Exception {
        assertTrue(Transformer.transform("SELECT (a || b) || c FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testConcatProblem2_5_2() throws Exception {
        assertTrue(Transformer.transform("SELECT (a + b) + c FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testConcatProblem2_6() throws Exception {
        assertTrue(Transformer.transform("SELECT max(a || b || c) FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMatches() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM team WHERE team.search_column @@ to_tsquery('new & york & yankees')", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testGroupByExpression() throws Exception {
        assertTrue(Transformer.transform(" GROUP BY col1, col2, col1  col2", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testBitwise() throws Exception {
        assertTrue(Transformer.transform("SELECT col1 & 32, col2 ^ col1, col1 | col2 +  FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectFunction() throws Exception {
        assertTrue(Transformer.transform("SELECT 1 + 2 AS sum", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWeirdSelect() throws Exception {
        assertTrue(Transformer.transform("String sql = select r.reviews_id, substring(rd.reviews_text, 100) as reviews_text, r.reviews_rating, r.date_added, r.customers_name from reviews r, reviews_description rd where r.products_id = '19' and r.reviews_id = rd.reviews_id and rd.languages_id = '1' and r.reviews_status = 1 order by r.reviews_id desc limit 0, 6", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCast() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST(a AS varchar) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT CAST(a AS varchar2) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastInCast() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST(CAST(a AS numeric) AS varchar) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastInCast2() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST('test' + CAST(assertEqual AS numeric) AS varchar) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastTypeProblem() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST(col1 AS varchar (256)) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastTypeProblem2() throws Exception {
        assertTrue(Transformer.transform("SELECT col1::varchar FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTryCast() throws Exception {
        assertTrue(Transformer.transform("SELECT TRY_CAST(a AS varchar) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT CAST(a AS varchar2) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTryCastInTryCast() throws Exception {
        assertTrue(Transformer.transform("SELECT TRY_CAST(TRY_CAST(a AS numeric) AS varchar) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTryCastInTryCast2() throws Exception {
        assertTrue(Transformer.transform("SELECT TRY_CAST('test' + TRY_CAST(assertEqual AS numeric) AS varchar) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTryCastTypeProblem() throws Exception {
        assertTrue(Transformer.transform("SELECT TRY_CAST(col1 AS varchar (256)) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMySQLHintStraightJoin() throws Exception {
        assertTrue(Transformer.transform("SELECT col FROM tbl STRAIGHT_JOIN tbl2 ON tbl.id = tbl2.id", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testStraightJoinInSelect() throws Exception {
        assertTrue(Transformer.transform("SELECT STRAIGHT_JOIN col, col2 FROM tbl INNER JOIN tbl2 ON tbl.id = tbl2.id", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastTypeProblem3() throws Exception {
        assertTrue(Transformer.transform("SELECT col1::varchar (256) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastTypeProblem4() throws Exception {
        assertTrue(Transformer.transform("SELECT 5::varchar (256) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastTypeProblem5() throws Exception {
        assertTrue(Transformer.transform("SELECT 5.67::varchar (256) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastTypeProblem6() throws Exception {
        assertTrue(Transformer.transform("SELECT 'test'::character varying FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastTypeProblem7() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST('test' AS character varying) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastTypeProblem8() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST('123' AS double precision) FROM tabelle1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCaseElseAddition() throws Exception {
        assertTrue(Transformer.transform("SELECT CASE WHEN 1 + 3 > 20 THEN 0 ELSE 1000 + 1 END AS d FROM dual", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testBrackets() throws Exception {
        assertTrue(Transformer.transform("SELECT table_a.name AS [Test] FROM table_a", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testBrackets2() throws Exception {
        assertTrue(Transformer.transform("SELECT [a] FROM t", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue1595() throws Exception {
    }

    @Test
    public void testBrackets3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM \"2016\"", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlServer_Modulo_Proz() throws Exception {
        assertTrue(Transformer.transform("SELECT 5 % 2 FROM A", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlServer_Modulo_mod() throws Exception {
        assertTrue(Transformer.transform("SELECT mod(5, 2) FROM A", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlServer_Modulo() throws Exception {
        assertTrue(Transformer.transform("SELECT convert(varchar(255), DATEDIFF(month, year1, abc_datum) / 12) + ' year, ' + convert(varchar(255), DATEDIFF(month, year2, abc_datum) % 12) + ' month' FROM test_table", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIsNot() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM test WHERE a IS NOT NULL", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIsNot2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM test WHERE NOT a IS NULL", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlAnalytic() throws Exception {
        assertTrue(Transformer.transform("SELECT a, row_number() OVER (ORDER BY a) AS n FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlAnalytic2() throws Exception {
        assertTrue(Transformer.transform("SELECT a, row_number() OVER (ORDER BY a, b) AS n FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlAnalytic3() throws Exception {
        assertTrue(Transformer.transform("SELECT a, row_number() OVER (PARTITION BY c ORDER BY a, b) AS n FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlAnalytic4EmptyOver() throws Exception {
        assertTrue(Transformer.transform("SELECT a, row_number() OVER () AS n FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlAnalytic5AggregateColumnValue() throws Exception {
        assertTrue(Transformer.transform("SELECT a, sum(b) OVER () AS n FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlAnalytic6AggregateColumnValue() throws Exception {
        assertTrue(Transformer.transform("SELECT a, sum(b + 5) OVER (ORDER BY a) AS n FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlAnalytic7Count() throws Exception {
        assertTrue(Transformer.transform("SELECT count(*) OVER () AS n FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlAnalytic8Complex() throws Exception {
        assertTrue(Transformer.transform("SELECT ID, NAME, SALARY, SUM(SALARY) OVER () AS SUM_SAL, AVG(SALARY) OVER () AS AVG_SAL, MIN(SALARY) OVER () AS MIN_SAL, MAX(SALARY) OVER () AS MAX_SAL, COUNT(*) OVER () AS ROWS2 FROM STAFF WHERE ID < 60 ORDER BY ID", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlAnalytic9CommaListPartition() throws Exception {
        assertTrue(Transformer.transform("SELECT a, row_number() OVER (PARTITION BY c, d ORDER BY a, b) AS n FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlAnalytic10Lag() throws Exception {
        assertTrue(Transformer.transform("SELECT a, lag(a, 1) OVER (PARTITION BY c ORDER BY a, b) AS n FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlAnalytic11Lag() throws Exception {
        assertTrue(Transformer.transform("SELECT a, lag(a, 1, 0) OVER (PARTITION BY c ORDER BY a, b) AS n FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunction12() throws Exception {
        assertTrue(Transformer.transform("SELECT SUM(a) OVER (PARTITION BY b ORDER BY c) FROM tab1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunction13() throws Exception {
        assertTrue(Transformer.transform("SELECT SUM(a) OVER () FROM tab1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunction14() throws Exception {
        assertTrue(Transformer.transform("SELECT SUM(a) OVER (PARTITION BY b ) FROM tab1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunction15() throws Exception {
        assertTrue(Transformer.transform("SELECT SUM(a) OVER (ORDER BY c) FROM tab1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunction16() throws Exception {
        assertTrue(Transformer.transform("SELECT SUM(a) OVER (ORDER BY c NULLS FIRST) FROM tab1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunction17() throws Exception {
        assertTrue(Transformer.transform("SELECT AVG(sal) OVER (PARTITION BY deptno ORDER BY sal ROWS BETWEEN 0 PRECEDING AND 0 PRECEDING) AS avg_of_current_sal FROM emp", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunction18() throws Exception {
        assertTrue(Transformer.transform("SELECT AVG(sal) OVER (PARTITION BY deptno ORDER BY sal RANGE CURRENT ROW) AS avg_of_current_sal FROM emp", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunctionProblem1() throws Exception {
        assertTrue(Transformer.transform("SELECT last_value(s.revenue_hold) OVER (PARTITION BY s.id_d_insertion_order, s.id_d_product_ad_attr, trunc(s.date_id, 'mm') ORDER BY s.date_id) AS col FROM s", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunction19() throws Exception {
        assertTrue(Transformer.transform("SELECT count(DISTINCT CASE WHEN client_organic_search_drop_flag = 1 THEN brand END) OVER (PARTITION BY client, category_1, category_2, category_3, category_4 ) AS client_brand_org_drop_count FROM sometable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunctionProblem1b() throws Exception {
        assertTrue(Transformer.transform("SELECT last_value(s.revenue_hold) OVER (PARTITION BY s.id_d_insertion_order, s.id_d_product_ad_attr, trunc(s.date_id, 'mm') ORDER BY s.date_id ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) AS col FROM s", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunctionIssue670() throws Exception {
        assertTrue(Transformer.transform("SELECT last_value(some_column IGNORE NULLS) OVER (PARTITION BY some_other_column_1, some_other_column_2 ORDER BY some_other_column_3 ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) column_alias FROM some_table", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunctionFilterIssue866() throws Exception {
        assertTrue(Transformer.transform("SELECT COUNT(*) FILTER (WHERE name = 'Raj') OVER (PARTITION BY name ) FROM table", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticPartitionBooleanExpressionIssue864() throws Exception {
        assertTrue(Transformer.transform("SELECT COUNT(*) OVER (PARTITION BY (event = 'admit' OR event = 'family visit') ORDER BY day ROWS BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING) family_visits FROM patients", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticPartitionBooleanExpressionIssue864_2() throws Exception {
        assertTrue(Transformer.transform("SELECT COUNT(*) OVER (PARTITION BY (event = 'admit' OR event = 'family visit') ) family_visits FROM patients", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnalyticFunctionFilterIssue934() throws Exception {
        assertTrue(Transformer.transform("SELECT COUNT(*) FILTER (WHERE name = 'Raj') FROM table", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFunctionLeft() throws Exception {
        assertTrue(Transformer.transform("SELECT left(table1.col1, 4) FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFunctionRight() throws Exception {
        assertTrue(Transformer.transform("SELECT right(table1.col1, 4) FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOneColumnFullTextSearchMySQL() throws Exception {
        assertTrue(Transformer.transform("SELECT MATCH (col1) AGAINST ('test' IN NATURAL LANGUAGE MODE) relevance FROM tbl", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSeveralColumnsFullTextSearchMySQL() throws Exception {
        assertTrue(Transformer.transform("SELECT MATCH (col1,col2,col3) AGAINST ('test' IN NATURAL LANGUAGE MODE) relevance FROM tbl", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFullTextSearchInDefaultMode() throws Exception {
        assertTrue(Transformer.transform("SELECT col FROM tbl WHERE MATCH (col1,col2,col3) AGAINST ('test') ORDER BY col", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIsTrue() throws Exception {
        assertTrue(Transformer.transform("SELECT col FROM tbl WHERE col IS TRUE", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIsFalse() throws Exception {
        assertTrue(Transformer.transform("SELECT col FROM tbl WHERE col IS FALSE", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIsNotTrue() throws Exception {
        assertTrue(Transformer.transform("SELECT col FROM tbl WHERE col IS NOT TRUE", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIsNotFalse() throws Exception {
        assertTrue(Transformer.transform("SELECT col FROM tbl WHERE col IS NOT FALSE", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOracleJoin() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tabelle1, tabelle2 WHERE tabelle1.a = tabelle2.b(+)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOracleJoin2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tabelle1, tabelle2 WHERE tabelle1.a(+) = tabelle2.b", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOracleJoin2_1() {
    }

    @Test
    public void testOracleJoin2_2() {
    }

    @Test
    public void testOracleJoin3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tabelle1, tabelle2 WHERE tabelle1.a(+) > tabelle2.b", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOracleJoin3_1() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tabelle1, tabelle2 WHERE tabelle1.a > tabelle2.b(+)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOracleJoin4() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tabelle1, tabelle2 WHERE tabelle1.a(+) = tabelle2.b AND tabelle1.b(+) IN ('A', 'B')", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOracleJoinIssue318() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM TBL_A, TBL_B, TBL_C WHERE TBL_A.ID(+) = TBL_B.ID AND TBL_C.ROOM(+) = TBL_B.ROOM", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlIntersect() throws Exception {
        assertTrue(Transformer.transform("(SELECT * FROM a) INTERSECT (SELECT * FROM b)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM a INTERSECT SELECT * FROM b", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIntegerDivOperator() throws Exception {
        assertTrue(Transformer.transform("SELECT col DIV 3", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlExcept() throws Exception {
        assertTrue(Transformer.transform("(SELECT * FROM a) EXCEPT (SELECT * FROM b)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM a EXCEPT SELECT * FROM b", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlMinus() throws Exception {
        assertTrue(Transformer.transform("(SELECT * FROM a) MINUS (SELECT * FROM b)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM a MINUS SELECT * FROM b", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlCombinedSets() throws Exception {
        assertTrue(Transformer.transform("(SELECT * FROM a) INTERSECT (SELECT * FROM b) UNION (SELECT * FROM c)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWithStatement() throws Exception {
        assertTrue(Transformer.transform("WITH test AS (SELECT mslink FROM feature) SELECT * FROM feature WHERE mslink IN (SELECT mslink FROM test)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSubjoinWithJoins() throws Exception {
        assertTrue(Transformer.transform("SELECT COUNT(DISTINCT `tbl1`.`id`) FROM (`tbl1`, `tbl2`, `tbl3`)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWithUnionProblem() throws Exception {
        assertTrue(Transformer.transform("WITH test AS ((SELECT mslink FROM tablea) UNION (SELECT mslink FROM tableb)) SELECT * FROM tablea WHERE mslink IN (SELECT mslink FROM test)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWithUnionAllProblem() throws Exception {
        assertTrue(Transformer.transform("WITH test AS ((SELECT mslink FROM tablea) UNION ALL (SELECT mslink FROM tableb)) SELECT * FROM tablea WHERE mslink IN (SELECT mslink FROM test)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWithUnionProblem3() throws Exception {
        assertTrue(Transformer.transform("WITH test AS ((SELECT mslink, CAST(tablea.fname AS varchar) FROM tablea INNER JOIN tableb ON tablea.mslink = tableb.mslink AND tableb.deleted = 0 WHERE tablea.fname IS NULL AND 1 = 0) UNION ALL (SELECT mslink FROM tableb)) SELECT * FROM tablea WHERE mslink IN (SELECT mslink FROM test)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWithUnionProblem4() throws Exception {
        assertTrue(Transformer.transform("WITH hist AS ((SELECT gl.mslink, ba.gl_name AS txt, ba.gl_nummer AS nr, 0 AS level, CAST(gl.mslink AS VARCHAR) AS path, ae.feature FROM tablea AS gl INNER JOIN tableb AS ba ON gl.mslink = ba.gl_mslink INNER JOIN tablec AS ae ON gl.mslink = ae.mslink AND ae.deleted = 0 WHERE gl.parent IS NULL AND gl.mslink <> 0) UNION ALL (SELECT gl.mslink, ba.gl_name AS txt, ba.gl_nummer AS nr, hist.level + 1 AS level, CAST(hist.path + '.' + CAST(gl.mslink AS VARCHAR) AS VARCHAR) AS path, ae.feature FROM tablea AS gl INNER JOIN tableb AS ba ON gl.mslink = ba.gl_mslink INNER JOIN tablec AS ae ON gl.mslink = ae.mslink AND ae.deleted = 0 INNER JOIN hist ON gl.parent = hist.mslink WHERE gl.mslink <> 0)) SELECT mslink, space(level * 4) + txt AS txt, nr, feature, path FROM hist WHERE EXISTS (SELECT feature FROM tablec WHERE mslink = 0 AND ((feature IN (1, 2) AND hist.feature = 3) OR (feature IN (4) AND hist.feature = 2)))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWithUnionProblem5() throws Exception {
        assertTrue(Transformer.transform("WITH hist AS ((SELECT gl.mslink, ba.gl_name AS txt, ba.gl_nummer AS nr, 0 AS level, CAST(gl.mslink AS VARCHAR) AS path, ae.feature FROM tablea AS gl INNER JOIN tableb AS ba ON gl.mslink = ba.gl_mslink INNER JOIN tablec AS ae ON gl.mslink = ae.mslink AND ae.deleted = 0 WHERE gl.parent IS NULL AND gl.mslink <> 0) UNION ALL (SELECT gl.mslink, ba.gl_name AS txt, ba.gl_nummer AS nr, hist.level + 1 AS level, CAST(hist.path + '.' + CAST(gl.mslink AS VARCHAR) AS VARCHAR) AS path, 5 AS feature FROM tablea AS gl INNER JOIN tableb AS ba ON gl.mslink = ba.gl_mslink INNER JOIN tablec AS ae ON gl.mslink = ae.mslink AND ae.deleted = 0 INNER JOIN hist ON gl.parent = hist.mslink WHERE gl.mslink <> 0)) SELECT * FROM hist", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testExtractFrom1() throws Exception {
        assertTrue(Transformer.transform("SELECT EXTRACT(month FROM datecolumn) FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testExtractFrom2() throws Exception {
        assertTrue(Transformer.transform("SELECT EXTRACT(year FROM now()) FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testExtractFrom3() throws Exception {
        assertTrue(Transformer.transform("SELECT EXTRACT(year FROM (now() - 2)) FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testExtractFrom4() throws Exception {
        assertTrue(Transformer.transform("SELECT EXTRACT(minutes FROM now() - '01:22:00') FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemFunction() throws Exception {
        assertTrue(Transformer.transform("SELECT test() FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemFunction2() throws Exception {
        assertTrue(Transformer.transform("SELECT sysdate FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemFunction3() throws Exception {
        assertTrue(Transformer.transform("SELECT TRUNCATE(col) FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAdditionalLettersGerman() throws Exception {
        assertTrue(Transformer.transform("SELECT colä, colö, colü FROM testtableäöü", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT colA, colÖ, colÜ FROM testtableÄÖÜ", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT Äcol FROM testtableÄÖÜ", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT ßcolß FROM testtableß", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAdditionalLettersSpanish() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM años", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiTableJoin() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM taba INNER JOIN tabb ON taba.a = tabb.a, tabc LEFT JOIN tabd ON tabc.c = tabd.c", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTableCrossJoin() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM taba CROSS JOIN tabb", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLateral1() throws Exception {
        assertTrue(Transformer.transform("SELECT O.ORDERID, O.CUSTNAME, OL.LINETOTAL FROM ORDERS AS O, LATERAL(SELECT SUM(NETAMT) AS LINETOTAL FROM ORDERLINES AS LINES WHERE LINES.ORDERID = O.ORDERID) AS OL", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLateralComplex1() throws IOException, JSQLParserException {
    }

    @Test
    public void testValues() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM (VALUES (1, 2), (3, 4)) AS test", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testValues2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM (VALUES 1, 2, 3, 4) AS test", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testValues3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM (VALUES 1, 2, 3, 4) AS test(a)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testValues4() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM (VALUES (1, 2), (3, 4)) AS test(a, b)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testValues5() throws Exception {
        assertTrue(Transformer.transform("SELECT X, Y FROM (VALUES (0, 'a'), (1, 'b')) AS MY_TEMP_TABLE(X, Y)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testValues6BothVariants() throws Exception {
        assertTrue(Transformer.transform("SELECT I FROM (VALUES 1, 2, 3) AS MY_TEMP_TABLE(I) WHERE I IN (SELECT * FROM (VALUES 1, 2) AS TEST)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIntervalWithColumn() throws Exception {
        assertTrue(Transformer.transform("SELECT DATE_ADD(start_date, INTERVAL duration MINUTE) AS end_datetime FROM appointment", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIntervalWithFunction() throws Exception {
        assertTrue(Transformer.transform("SELECT DATE_ADD(start_date, INTERVAL COALESCE(duration, 21) MINUTE) AS end_datetime FROM appointment", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testInterval1() throws Exception {
        assertTrue(Transformer.transform("SELECT 5 + INTERVAL '3 days'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testInterval2() throws Exception {
        assertTrue(Transformer.transform("SELECT to_timestamp(to_char(now() - INTERVAL '45 MINUTE', 'YYYY-MM-DD-HH24:')) AS START_TIME FROM tab1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testInterval3() throws Exception {
        assertTrue(Transformer.transform("SELECT 5 + INTERVAL '3' day", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testInterval4() throws Exception {
        assertTrue(Transformer.transform("SELECT '2008-12-31 23:59:59' + INTERVAL 1 SECOND", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testInterval5_Issue228() throws Exception {
        assertTrue(Transformer.transform("SELECT ADDDATE(timeColumn1, INTERVAL 420 MINUTES) AS timeColumn1 FROM tbl", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT ADDDATE(timeColumn1, INTERVAL -420 MINUTES) AS timeColumn1 FROM tbl", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiValueIn() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (a, b, c) IN (SELECT a, b, c FROM mytable2)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiValueIn2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (trim(a), trim(b)) IN (SELECT a, b FROM mytable2)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiValueIn3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (SSN, SSM) IN (('11111111111111', '22222222222222'))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiValueIn_withAnd() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (SSN, SSM) IN (('11111111111111', '22222222222222')) AND 1 = 1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiValueIn4() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (a, b) IN ((1, 2), (3, 4), (5, 6), (7, 8))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void selectIsolationKeywordsAsAlias() throws Exception {
        assertTrue(Transformer.transform("SELECT col FROM tbl cs", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiValueInBinds() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (a, b) IN ((?, ?), (?, ?))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testUnionWithBracketsAndOrderBy() throws Exception {
        assertTrue(Transformer.transform("(SELECT a FROM tbl ORDER BY a) UNION DISTINCT (SELECT a FROM tbl ORDER BY a)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiValueNotInBinds() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (a, b) NOT IN ((?, ?), (?, ?))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiValueIn_NTuples() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (a, b, c, d, e) IN ((1, 2, 3, 4, 5), (6, 7, 8, 9, 10), (11, 12, 13, 14, 15))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivot1() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable PIVOT (count(a) FOR b IN ('val1'))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivot2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable PIVOT (count(a) FOR b IN (10, 20, 30))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivot3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable PIVOT (count(a) AS vals FOR b IN (10 AS d1, 20, 30 AS d3))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivot4() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable PIVOT (count(a), sum(b) FOR b IN (10, 20, 30))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivot5() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable PIVOT (count(a) FOR (b, c) IN ((10, 'a'), (20, 'b'), (30, 'c')))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivotXml1() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable PIVOT XML (count(a) FOR b IN ('val1'))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivotXml2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable PIVOT XML (count(a) FOR b IN (SELECT vals FROM myothertable))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivotXml3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable PIVOT XML (count(a) FOR b IN (ANY))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivotXmlSubquery1() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM (SELECT times_purchased, state_code FROM customers t) PIVOT (count(state_code) FOR state_code IN ('NY', 'CT', 'NJ', 'FL', 'MO')) ORDER BY times_purchased", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivotFunction() throws Exception {
        assertTrue(Transformer.transform("SELECT to_char((SELECT col1 FROM (SELECT times_purchased, state_code FROM customers t) PIVOT (count(state_code) FOR state_code IN ('NY', 'CT', 'NJ', 'FL', 'MO')) ORDER BY times_purchased)) FROM DUAL", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testUnPivotWithAlias() throws Exception {
    }

    @Test
    public void testUnPivot() throws Exception {
        assertTrue(Transformer.transform("  FOR product_code IN (product_a AS 'A', product_b AS 'B', product_c AS 'C'))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testUnPivotWithMultiColumn() throws Exception {
        assertTrue(Transformer.transform("  FOR product_code IN ((product_a, product_1) AS 'A', (product_b, product_2) AS 'B', (product_c, product_3) AS 'C'))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivotWithAlias() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM (SELECT * FROM mytable LEFT JOIN mytable2 ON Factor_ID = Id) f PIVOT (max(f.value) FOR f.factoryCode IN (ZD, COD, SW, PH))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivotWithAlias2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM (SELECT * FROM mytable LEFT JOIN mytable2 ON Factor_ID = Id) f PIVOT (max(f.value) FOR f.factoryCode IN (ZD, COD, SW, PH)) d", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivotWithAlias3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM (SELECT * FROM mytable LEFT JOIN mytable2 ON Factor_ID = Id) PIVOT (max(f.value) FOR f.factoryCode IN (ZD, COD, SW, PH)) d", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPivotWithAlias4() throws Exception {
    }

    @Test
    public void testRegexpLike1() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE REGEXP_LIKE(first_name, '^Ste(v|ph)en$')", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testRegexpLike2() throws Exception {
        assertTrue(Transformer.transform("SELECT CASE WHEN REGEXP_LIKE(first_name, '^Ste(v|ph)en$') THEN 1 ELSE 2 END FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testRegexpMySQL() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE first_name REGEXP '^Ste(v|ph)en$'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotRegexpMySQLIssue887() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE first_name NOT REGEXP '^Ste(v|ph)en$'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotRegexpMySQLIssue887_2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE NOT first_name REGEXP '^Ste(v|ph)en$'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testRegexpBinaryMySQL() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE first_name REGEXP BINARY '^Ste(v|ph)en$'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testXorCondition() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE field = value XOR other_value", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testRlike() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE first_name RLIKE '^Ste(v|ph)en$'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testBooleanFunction1() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE test_func(col1)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNamedParameter() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE b = :param", List.of(new SqlTypedParamValue("String", "b"))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNamedParameter2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE a = :param OR a = :param2 AND b = :param3", List.of(new SqlTypedParamValue("String", "a"), new SqlTypedParamValue("String", "b"), new SqlTypedParamValue("String", "c"))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNamedParameter3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM t WHERE c = :from", List.of(new SqlTypedParamValue("String", "a"))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testComplexUnion1() throws IOException, JSQLParserException {
        assertTrue(Transformer.transform("(SELECT 'abc-' || coalesce(mytab.a::varchar, '') AS a, mytab.b, mytab.c AS st, mytab.d, mytab.e FROM mytab WHERE mytab.del = 0) UNION (SELECT 'cde-' || coalesce(mytab2.a::varchar, '') AS a, mytab2.b, mytab2.bezeichnung AS c, 0 AS d, 0 AS e FROM mytab2 WHERE mytab2.del = 0)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOracleHierarchicalQuery() throws Exception {
        assertTrue(Transformer.transform("SELECT last_name, employee_id, manager_id FROM employees CONNECT BY employee_id = manager_id ORDER BY last_name", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOracleHierarchicalQuery2() throws Exception {
        assertTrue(Transformer.transform("SELECT employee_id, last_name, manager_id FROM employees CONNECT BY PRIOR employee_id = manager_id", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOracleHierarchicalQuery3() throws Exception {
        assertTrue(Transformer.transform("SELECT last_name, employee_id, manager_id, LEVEL FROM employees START WITH employee_id = 100 CONNECT BY PRIOR employee_id = manager_id ORDER SIBLINGS BY last_name", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOracleHierarchicalQuery4() throws Exception {
        assertTrue(Transformer.transform("SELECT last_name, employee_id, manager_id, LEVEL FROM employees CONNECT BY PRIOR employee_id = manager_id START WITH employee_id = 100 ORDER SIBLINGS BY last_name", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOracleHierarchicalQueryIssue196() throws Exception {
        assertTrue(Transformer.transform("SELECT num1, num2, level FROM carol_tmp START WITH num2 = 1008 CONNECT BY num2 = PRIOR num1 ORDER BY level DESC", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPostgreSQLRegExpCaseSensitiveMatch() throws Exception {
        assertTrue(Transformer.transform("SELECT a, b FROM foo WHERE a ~ '[help].*'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPostgreSQLRegExpCaseSensitiveMatch2() throws Exception {
        assertTrue(Transformer.transform("SELECT a, b FROM foo WHERE a ~* '[help].*'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPostgreSQLRegExpCaseSensitiveMatch3() throws Exception {
        assertTrue(Transformer.transform("SELECT a, b FROM foo WHERE a !~ '[help].*'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPostgreSQLRegExpCaseSensitiveMatch4() throws Exception {
        assertTrue(Transformer.transform("SELECT a, b FROM foo WHERE a !~* '[help].*'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testReservedKeyword() throws Exception {
        assertTrue(Transformer.transform("SELECT cast, do, extract, first, following, last, materialized, nulls, partition, range, row, rows, siblings, value, xml FROM tableName // all of these are legal in SQL server 'row' and 'rows' are not legal on Oracle, though", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testReservedKeyword2() throws Exception {
        assertTrue(Transformer.transform("SELECT open FROM tableName", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testReservedKeyword3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable1 t JOIN mytable2 AS prior ON t.id = prior.id", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCharacterSetClause() throws Exception {
        assertTrue(Transformer.transform("SELECT DISTINCT CAST(`view0`.`nick2` AS CHAR (8000) CHARACTER SET utf8) AS `v0` FROM people `view0` WHERE `view0`.`nick2` IS NOT NULL", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotEqualsTo() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM foo WHERE a != b", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM foo WHERE a <> b", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testGeometryDistance() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM foo ORDER BY a <-> b", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM foo ORDER BY a <#> b", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testJsonExpression() throws Exception {
        assertTrue(Transformer.transform("SELECT data->'images'->'thumbnail'->'url' AS thumb FROM instagram", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM sales WHERE sale->'items'->>'description' = 'milk'", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM sales WHERE sale->'items'->>'quantity' = 12::TEXT", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT SUM(CAST(sale->'items'->>'quantity' AS integer)) AS total_quantity_sold FROM sales", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT sale->>'items' FROM sales", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT json_typeof(sale->'items'), json_typeof(sale->'items'->'quantity') FROM sales", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testJsonExpressionWithCastExpression() throws Exception {
        assertTrue(Transformer.transform("SELECT id FROM tbl WHERE p.company::json->'info'->>'country' = 'test'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testJsonExpressionWithIntegerParameterIssue909() throws Exception {
    }

    @Test
    public void testSqlNoCache() throws Exception {
        assertTrue(Transformer.transform("SELECT SQL_NO_CACHE sales.date FROM sales", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSqlCache() throws Exception {
        assertTrue(Transformer.transform("SELECT SQL_CACHE sales.date FROM sales", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectInto1() throws Exception {
        assertTrue(Transformer.transform("SELECT * INTO user_copy FROM user", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectForUpdate() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM user_table FOR UPDATE", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectForUpdate2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM emp WHERE empno = ? FOR UPDATE", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectJoin() throws Exception {
    }

    @Test
    public void testSelectJoin2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM pg_constraint WHERE pg_attribute.attnum = ANY(pg_constraint.conkey)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM pg_constraint WHERE pg_attribute.attnum = ALL(pg_constraint.conkey)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAnyConditionSubSelect() throws Exception {
    }

    @Test
    public void testAllConditionSubSelect() throws Exception {
    }

    @Test
    public void testSelectOracleColl() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM the_table tt WHERE TT.COL1 = lines(idx).COL1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectInnerWith() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM (WITH actor AS (SELECT 'a' aid FROM DUAL) SELECT aid FROM actor)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectInnerWithAndUnionIssue1084_2() throws Exception {
        assertTrue(Transformer.transform("WITH actor AS (SELECT 'b' aid FROM DUAL) SELECT aid FROM actor UNION SELECT aid FROM actor2", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectWithinGroup() throws Exception {
        assertTrue(Transformer.transform("SELECT LISTAGG(col1, '##') WITHIN GROUP (ORDER BY col1) FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectUserVariable() throws Exception {
        assertTrue(Transformer.transform("SELECT @col FROM t1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectNumericBind() throws Exception {
        assertTrue(Transformer.transform("SELECT a FROM b WHERE c = :1", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectBrackets() throws Exception {
        assertTrue(Transformer.transform("SELECT avg((123.250)::numeric)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectBrackets2() throws Exception {
        assertTrue(Transformer.transform("SELECT (EXTRACT(epoch FROM age(d1, d2)) / 2)::numeric", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectBrackets3() throws Exception {
        assertTrue(Transformer.transform("SELECT avg((EXTRACT(epoch FROM age(d1, d2)) / 2)::numeric)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectBrackets4() throws Exception {
        assertTrue(Transformer.transform("SELECT (1 / 2)::numeric", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectForUpdateOfTable() throws Exception {
        assertTrue(Transformer.transform("SELECT foo.*, bar.* FROM foo, bar WHERE foo.id = bar.foo_id FOR UPDATE OF foo", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectWithBrackets() throws Exception {
        assertTrue(Transformer.transform("(SELECT 1 FROM mytable)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectWithBrackets2() throws Exception {
        assertTrue(Transformer.transform("(SELECT 1)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectWithoutFrom() throws Exception {
        assertTrue(Transformer.transform("SELECT footable.foocolumn", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectKeywordPercent() throws Exception {
        assertTrue(Transformer.transform("SELECT percent FROM MY_TABLE", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectJPQLPositionalParameter() throws Exception {
        assertTrue(Transformer.transform("SELECT email FROM users WHERE (type LIKE 'B') AND (username LIKE ?1)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectKeep() throws Exception {
        assertTrue(Transformer.transform("SELECT col1, min(col2) KEEP (DENSE_RANK FIRST ORDER BY col3), col4 FROM table1 GROUP BY col5 ORDER BY col3", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectKeepOver() throws Exception {
        assertTrue(Transformer.transform("SELECT MIN(salary) KEEP (DENSE_RANK FIRST ORDER BY commission_pct) OVER (PARTITION BY department_id ) \"Worst\" FROM employees ORDER BY department_id, salary", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testGroupConcat() throws Exception {
        assertTrue(Transformer.transform("SELECT student_name, GROUP_CONCAT(DISTINCT test_score ORDER BY test_score DESC SEPARATOR ' ') FROM student GROUP BY student_name", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testRowConstructor1() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM t1 WHERE (col1, col2) = (SELECT col3, col4 FROM t2 WHERE id = 10)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testRowConstructor2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM t1 WHERE ROW(col1, col2) = (SELECT col3, col4 FROM t2 WHERE id = 10)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue154() throws Exception {
        assertTrue(Transformer.transform("SELECT d.id, d.uuid, d.name, d.amount, d.percentage, d.modified_time FROM discount d LEFT OUTER JOIN discount_category dc ON d.id = dc.discount_id WHERE merchant_id = ? AND deleted = ? AND dc.discount_id IS NULL AND modified_time < ? AND modified_time >= ? ORDER BY modified_time", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue154_2() throws Exception {
        assertTrue(Transformer.transform("SELECT r.id, r.uuid, r.name, r.system_role FROM role r WHERE r.merchant_id = ? AND r.deleted_time IS NULL ORDER BY r.id DESC", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue160_signedParameter() throws Exception {
        assertTrue(Transformer.transform("SELECT start_date WHERE start_date > DATEADD(HH, -?, GETDATE())", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue160_signedParameter2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE -? = 5", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue162_doubleUserVar() throws Exception {
        assertTrue(Transformer.transform("SELECT @@SPID AS ID, SYSTEM_USER AS \"Login Name\", USER AS \"User Name\"", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue167_singleQuoteEscape() throws Exception {
        assertTrue(Transformer.transform("SELECT 'a'", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT ''''", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT '\\''", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT 'ab''ab'", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT 'ab\\'ab'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue167_singleQuoteEscape2() throws Exception {
        assertTrue(Transformer.transform("SELECT '\\'''", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT '\\\\\\''", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue77_singleQuoteEscape2() throws Exception {
        assertTrue(Transformer.transform("SELECT 'test\\'' FROM dual", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue223_singleQuoteEscape() throws Exception {
        assertTrue(Transformer.transform("SELECT '\\'test\\''", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOracleHint() throws Exception {
    }

    @Test
    public void testOracleHintExpression() throws Exception {
        assertTrue(Transformer.transform("SELECT --+ HINT\n * FROM tab1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTableFunctionWithNoParams() throws Exception {
        assertTrue(Transformer.transform("SELECT f2 FROM SOME_FUNCTION()", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTableFunctionWithParams() throws Exception {
        assertTrue(Transformer.transform("SELECT f2 FROM SOME_FUNCTION(1, 'val')", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTableFunctionWithAlias() throws Exception {
        assertTrue(Transformer.transform("SELECT f2 FROM SOME_FUNCTION() AS z", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue151_tableFunction() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tables a LEFT JOIN getdata() b ON a.id = b.id", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue217_keywordSeparator() throws Exception {
        assertTrue(Transformer.transform("SELECT Separator", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue215_possibleEndlessParsing() throws Exception {
        assertTrue(Transformer.transform("SELECT (CASE WHEN ((value LIKE '%t1%') OR (value LIKE '%t2%')) THEN 't1s' WHEN ((((((((((((((((((((((((((((value LIKE '%t3%') OR (value LIKE '%t3%')) OR (value LIKE '%t3%')) OR (value LIKE '%t4%')) OR (value LIKE '%t4%')) OR (value LIKE '%t5%')) OR (value LIKE '%t6%')) OR (value LIKE '%t6%')) OR (value LIKE '%t7%')) OR (value LIKE '%t7%')) OR (value LIKE '%t7%')) OR (value LIKE '%t8%')) OR (value LIKE '%t8%')) OR (value LIKE '%CTO%')) OR (value LIKE '%cto%')) OR (value LIKE '%Cto%')) OR (value LIKE '%t9%')) OR (value LIKE '%t9%')) OR (value LIKE '%COO%')) OR (value LIKE '%coo%')) OR (value LIKE '%Coo%')) OR (value LIKE '%t10%')) OR (value LIKE '%t10%')) OR (value LIKE '%CIO%')) OR (value LIKE '%cio%')) OR (value LIKE '%Cio%')) OR (value LIKE '%t11%')) OR (value LIKE '%t11%')) THEN 't' WHEN ((((value LIKE '%t12%') OR (value LIKE '%t12%')) OR (value LIKE '%VP%')) OR (value LIKE '%vp%')) THEN 'Vice t12s' WHEN ((((((value LIKE '% IT %') OR (value LIKE '%t13%')) OR (value LIKE '%t13%')) OR (value LIKE '% it %')) OR (value LIKE '%tech%')) OR (value LIKE '%Tech%')) THEN 'IT' WHEN ((((value LIKE '%Analyst%') OR (value LIKE '%t14%')) OR (value LIKE '%Analytic%')) OR (value LIKE '%analytic%')) THEN 'Analysts' WHEN ((value LIKE '%Manager%') OR (value LIKE '%manager%')) THEN 't15' ELSE 'Other' END) FROM tab1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue215_possibleEndlessParsing2() throws Exception {
        assertTrue(Transformer.transform("SELECT (CASE WHEN ((value LIKE '%t1%') OR (value LIKE '%t2%')) THEN 't1s' ELSE 'Other' END) FROM tab1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue215_possibleEndlessParsing3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE ((((((((((((((((((((((((((((value LIKE '%t3%') OR (value LIKE '%t3%')) OR (value LIKE '%t3%')) OR (value LIKE '%t4%')) OR (value LIKE '%t4%')) OR (value LIKE '%t5%')) OR (value LIKE '%t6%')) OR (value LIKE '%t6%')) OR (value LIKE '%t7%')) OR (value LIKE '%t7%')) OR (value LIKE '%t7%')) OR (value LIKE '%t8%')) OR (value LIKE '%t8%')) OR (value LIKE '%CTO%')) OR (value LIKE '%cto%')) OR (value LIKE '%Cto%')) OR (value LIKE '%t9%')) OR (value LIKE '%t9%')) OR (value LIKE '%COO%')) OR (value LIKE '%coo%')) OR (value LIKE '%Coo%')) OR (value LIKE '%t10%')) OR (value LIKE '%t10%')) OR (value LIKE '%CIO%')) OR (value LIKE '%cio%')) OR (value LIKE '%Cio%')) OR (value LIKE '%t11%')) OR (value LIKE '%t11%'))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue215_possibleEndlessParsing4() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE ((value LIKE '%t3%') OR (value LIKE '%t3%'))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue215_possibleEndlessParsing5() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE ((((((value LIKE '%t3%') OR (value LIKE '%t3%')) OR (value LIKE '%t3%')) OR (value LIKE '%t4%')) OR (value LIKE '%t4%')) OR (value LIKE '%t5%'))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue215_possibleEndlessParsing6() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (((((((((((((value LIKE '%t3%') OR (value LIKE '%t3%')) OR (value LIKE '%t3%')) OR (value LIKE '%t4%')) OR (value LIKE '%t4%')) OR (value LIKE '%t5%')) OR (value LIKE '%t6%')) OR (value LIKE '%t6%')) OR (value LIKE '%t7%')) OR (value LIKE '%t7%')) OR (value LIKE '%t7%')) OR (value LIKE '%t8%')) OR (value LIKE '%t8%'))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue215_possibleEndlessParsing7() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (((((((((((((((((((((value LIKE '%t3%') OR (value LIKE '%t3%')) OR (value LIKE '%t3%')) OR (value LIKE '%t4%')) OR (value LIKE '%t4%')) OR (value LIKE '%t5%')) OR (value LIKE '%t6%')) OR (value LIKE '%t6%')) OR (value LIKE '%t7%')) OR (value LIKE '%t7%')) OR (value LIKE '%t7%')) OR (value LIKE '%t8%')) OR (value LIKE '%t8%')) OR (value LIKE '%CTO%')) OR (value LIKE '%cto%')) OR (value LIKE '%Cto%')) OR (value LIKE '%t9%')) OR (value LIKE '%t9%')) OR (value LIKE '%COO%')) OR (value LIKE '%coo%')) OR (value LIKE '%Coo%'))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue230_cascadeKeyword() throws Exception {
        assertTrue(Transformer.transform("SELECT t.cascade AS cas FROM t", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testBooleanValue() throws Exception {
        assertTrue(Transformer.transform("SELECT col FROM t WHERE a", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testBooleanValue2() throws Exception {
        assertTrue(Transformer.transform("SELECT col FROM t WHERE 3 < 5 AND a", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotWithoutParenthesisIssue234() throws Exception {
        assertTrue(Transformer.transform("SELECT count(*) FROM \"Persons\" WHERE NOT \"F_NAME\" = 'John'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWhereIssue240_1() throws Exception {
        assertTrue(Transformer.transform("SELECT count(*) FROM mytable WHERE 1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWhereIssue240_0() throws Exception {
        assertTrue(Transformer.transform("SELECT count(*) FROM mytable WHERE 0", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCaseKeyword() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM Case", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastToSignedInteger() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST(contact_id AS SIGNED INTEGER) FROM contact WHERE contact_id = 20", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastToSigned() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST(contact_id AS SIGNED) FROM contact WHERE contact_id = 20", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWhereIssue240_true() throws Exception {
        assertTrue(Transformer.transform("SELECT count(*) FROM mytable WHERE true", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWhereIssue240_false() throws Exception {
        assertTrue(Transformer.transform("SELECT count(*) FROM mytable WHERE false", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWhereIssue241KeywordEnd() throws Exception {
        assertTrue(Transformer.transform("SELECT l.end FROM lessons l", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSpeedTestIssue235() throws Exception {
    }

    @Test
    public void testSpeedTestIssue235_2() throws IOException, JSQLParserException {
    }

    @Test
    public void testCastVarCharMaxIssue245() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST('foo' AS NVARCHAR (MAX))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNestedFunctionCallIssue253() throws Exception {
        assertTrue(Transformer.transform("SELECT (replace_regex(replace_regex(replace_regex(get_json_string(a_column, 'value'), '\\n', ' '), '\\r', ' '), '\\\\', '\\\\\\\\')) FROM a_table WHERE b_column = 'value'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testEscapedBackslashIssue253() throws Exception {
        assertTrue(Transformer.transform("SELECT replace_regex('test', '\\\\', '\\\\\\\\')", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeywordTableIssue261() throws Exception {
        assertTrue(Transformer.transform("SELECT column_value FROM table(VARCHAR_LIST_TYPE())", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTopExpressionIssue243() throws Exception {
        assertTrue(Transformer.transform("SELECT TOP (? + 1) * FROM MyTable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTopExpressionIssue243_2() throws Exception {
        assertTrue(Transformer.transform("SELECT TOP (CAST(? AS INT)) * FROM MyTable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFunctionIssue284() throws Exception {
        assertTrue(Transformer.transform("SELECT NVL((SELECT 1 FROM DUAL), 1) AS A FROM TEST1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFunctionDateTimeValues() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab1 WHERE a > TIMESTAMP '2004-04-30 04:05:34.56'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPR73() throws Exception {
        assertTrue(Transformer.transform("SELECT date_part('day', TIMESTAMP '2001-02-16 20:38:40')", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT EXTRACT(year FROM DATE '2001-02-16')", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testUniqueInsteadOfDistinctIssue299() throws Exception {
    }

    @Test
    public void testProblemSqlIssue265() throws IOException, JSQLParserException {
    }

    @Test
    public void testProblemSqlIssue330() throws Exception {
    }

    @Test
    public void testProblemSqlIssue330_2() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST('90 days' AS interval)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemKeywordCommitIssue341() throws Exception {
        assertTrue(Transformer.transform("SELECT id, commit FROM table1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlIssue352() throws Exception {
    }

    @Test
    public void testProblemIsIssue331() throws Exception {
    }

    @Test
    public void testProblemIssue375() throws Exception {
    }

    @Test
    public void testProblemIssue375Simplified() throws Exception {
    }

    @Test
    public void testProblemIssue375Simplified2() throws Exception {
    }

    @Test
    public void testProblemInNotInProblemIssue379() throws Exception {
        assertTrue(Transformer.transform("SELECT rank FROM DBObjects WHERE rank NOT IN (0, 1)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT rank FROM DBObjects WHERE rank IN (0, 1)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemLargeNumbersIssue390() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM student WHERE student_no = 20161114000000035001", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeyWorkInsertIssue393() throws Exception {
        assertTrue(Transformer.transform("SELECT insert(\"aaaabbb\", 4, 4, \"****\")", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeyWorkReplaceIssue393() throws Exception {
        assertTrue(Transformer.transform("SELECT replace(\"aaaabbb\", 4, 4, \"****\")", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testForUpdateWaitParseDeparse() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable FOR UPDATE WAIT 60", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testForUpdateWaitWithTimeout() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable FOR UPDATE WAIT 60", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testForUpdateNoWait() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable FOR UPDATE NOWAIT", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMysqlIndexHints() throws Exception {
        assertTrue(Transformer.transform("SELECT column FROM testtable AS t0 USE INDEX (index1)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT column FROM testtable AS t0 IGNORE INDEX (index1)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT column FROM testtable AS t0 FORCE INDEX (index1)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMysqlIndexHintsWithJoins() throws Exception {
        assertTrue(Transformer.transform("SELECT column FROM table0 t0 INNER JOIN table1 t1 USE INDEX (index1)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT column FROM table0 t0 INNER JOIN table1 t1 IGNORE INDEX (index1)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT column FROM table0 t0 INNER JOIN table1 t1 FORCE INDEX (index1)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMysqlMultipleIndexHints() throws Exception {
        assertTrue(Transformer.transform("SELECT column FROM testtable AS t0 USE INDEX (index1,index2)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT column FROM testtable AS t0 IGNORE INDEX (index1,index2)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT column FROM testtable AS t0 FORCE INDEX (index1,index2)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSqlServerHints() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM TB_Sys_Pedido WITH (NOLOCK) WHERE ID_Pedido = :ID_Pedido", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSqlServerHintsWithIndexIssue915() throws Exception {
        assertTrue(Transformer.transform("SELECT 1 FROM tableName1 WITH (INDEX (idx1), NOLOCK)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSqlServerHintsWithIndexIssue915_2() throws Exception {
        assertTrue(Transformer.transform("SELECT 1 FROM tableName1 AS t1 WITH (INDEX (idx1)) JOIN tableName2 AS t2 WITH (INDEX (idx2)) ON t1.id = t2.id", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemIssue435() throws Exception {
        assertTrue(Transformer.transform("SELECT if(z, 'a', 'b') AS business_type FROM mytable1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemIssue437Index() throws Exception {
    }

    @Test
    public void testProblemIssue445() throws Exception {
        assertTrue(Transformer.transform("SELECT E.ID_NUMBER, row_number() OVER (PARTITION BY E.ID_NUMBER ORDER BY E.DEFINED_UPDATED DESC) rn FROM T_EMPLOYMENT E", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemIssue485Date() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tab WHERE tab.date = :date", List.of(new SqlTypedParamValue("String", "2022-09-26"))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testGroupByProblemIssue482() throws Exception {
        assertTrue(Transformer.transform("SELECT SUM(orderTotalValue) AS value, MONTH(invoiceDate) AS month, YEAR(invoiceDate) AS year FROM invoice.Invoices WHERE projectID = 1 GROUP BY MONTH(invoiceDate), YEAR(invoiceDate) ORDER BY YEAR(invoiceDate) DESC, MONTH(invoiceDate) DESC", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue512() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM #tab1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM tab#tab1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue512_2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM $tab1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM #$tab#tab1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM #$tab1#", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM $#tab1#", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue514() throws Exception {
        assertTrue(Transformer.transform("SELECT listagg(c1, ';') WITHIN GROUP (PARTITION BY 1 ORDER BY 1) col FROM dual", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue508LeftRightBitwiseShift() throws Exception {
        assertTrue(Transformer.transform("SELECT 1 << 1", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT 1 >> 1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue522() throws Exception {
    }

    @Test
    public void testIssue522_2() throws Exception {
        assertTrue(Transformer.transform("SELECT -1 * SIGN(mr.quantity_issued) FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue522_3() throws Exception {
    }

    @Test
    public void testIssue522_4() throws Exception {
        assertTrue(Transformer.transform("SELECT CASE a + b WHEN -1 * 5 THEN 1 ELSE CASE b + c WHEN -1 * 6 THEN 2 ELSE 3 END END", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue554() throws Exception {
        assertTrue(Transformer.transform("SELECT T.INDEX AS INDEX133_ FROM myTable T", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue567KeywordPrimary() throws Exception {
        assertTrue(Transformer.transform("SELECT primary, secondary FROM info", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue572TaskReplacement() throws Exception {
        assertTrue(Transformer.transform("SELECT task_id AS \"Task Id\" FROM testtable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue566LargeView() throws IOException, JSQLParserException {
    }

    @Test
    public void testIssue566PostgreSQLEscaped() throws IOException, JSQLParserException {
        assertTrue(Transformer.transform("SELECT E'test'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testEscaped() throws IOException, JSQLParserException {
        assertTrue(Transformer.transform("SELECT _utf8'testvalue'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue563MultiSubJoin() throws Exception {
        assertTrue(Transformer.transform("SELECT c FROM ((SELECT a FROM t) JOIN (SELECT b FROM t2) ON a = B JOIN (SELECT c FROM t3) ON b = c)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue563MultiSubJoin_2() throws Exception {
        assertTrue(Transformer.transform("SELECT c FROM ((SELECT a FROM t))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue582NumericConstants() throws Exception {
        assertTrue(Transformer.transform("SELECT x'009fd'", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT X'009fd'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue583CharacterLiteralAsAlias() throws Exception {
        assertTrue(Transformer.transform("SELECT CASE WHEN T.ISC = 1 THEN T.EXTDESC WHEN T.b = 2 THEN '2' ELSE T.C END AS 'Test' FROM T", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue266KeywordTop() throws Exception {
        assertTrue(Transformer.transform("SELECT @top", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT @TOP", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue584MySQLValueListExpression() throws Exception {
        assertTrue(Transformer.transform("SELECT a, b FROM T WHERE (T.a, T.b) = (c, d)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT a FROM T WHERE (T.a) = (SELECT b FROM T, c, d)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue588NotNull() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE col1 ISNULL", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testParenthesisAroundFromItem() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM (mytable)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testParenthesisAroundFromItem2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM (mytable myalias)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testParenthesisAroundFromItem3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM (mytable) myalias", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testJoinerExpressionIssue596() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM a JOIN (b JOIN c ON b.id = c.id) ON a.id = c.id", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlIssue603() throws Exception {
        assertTrue(Transformer.transform("SELECT CASE WHEN MAX(CAST(a.jobNum AS INTEGER)) IS NULL THEN '1000' ELSE MAX(CAST(a.jobNum AS INTEGER)) + 1 END FROM user_employee a", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlIssue603_2() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST(col1 AS UNSIGNED INTEGER) FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblemSqlFuncParamIssue605() throws Exception {
    }

    @Test
    public void testProblemSqlFuncParamIssue605_2() throws Exception {
        assertTrue(Transformer.transform("SELECT func(SELECT col1 FROM mytable)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSqlContainIsNullFunctionShouldBeParsed() throws Exception {
        assertTrue(Transformer.transform("SELECT name, age, ISNULL(home, 'earn more money') FROM person", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNestedCast() throws Exception {
        assertTrue(Transformer.transform("SELECT acolumn::bit (64)::bigint FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testAndOperator() throws Exception {
        assertTrue(Transformer.transform("SELECT name from customers where name = 'John' && lastname = 'Doh'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNamedParametersIssue612() throws Exception {
        assertTrue(Transformer.transform("SELECT a FROM b LIMIT 10 OFFSET :param", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMissingOffsetIssue620() throws Exception {
        assertTrue(Transformer.transform("SELECT a, b FROM test OFFSET 0", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT a, b FROM test LIMIT 1 OFFSET 0", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartNames1() throws Exception {
        assertTrue(Transformer.transform("SELECT a.b", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartNames2() throws Exception {
        assertTrue(Transformer.transform("SELECT a.b.*", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartNames3() throws Exception {
        assertTrue(Transformer.transform("SELECT a.*", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartNames4() throws Exception {
        assertTrue(Transformer.transform("SELECT a.b.c.d.e.f.g.h", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartNames5() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM a.b.c.d.e.f.g.h", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartNamesIssue163() throws Exception {
        assertTrue(Transformer.transform("SELECT mymodel.name FROM com.myproject.MyModelClass AS mymodel", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartNamesIssue608() throws Exception {
        assertTrue(Transformer.transform("SELECT @@sessions.tx_read_only", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartNamesForFunctionsIssue944() throws Exception {
        assertTrue(Transformer.transform("SELECT pg_catalog.now()", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelContraction() throws Exception {
        assertTrue(Transformer.transform("SEL name, age FROM person", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT name, age FROM person", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartNamesIssue643() throws Exception {
        assertTrue(Transformer.transform("SELECT id, bid, pid, devnum, pointdesc, sysid, zone, sort FROM fault ORDER BY id DESC LIMIT ?, ?", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotNotIssue() throws Exception {
        assertTrue(Transformer.transform("SELECT VALUE1, VALUE2 FROM FOO WHERE NOT BAR LIKE '*%'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCharNotParsedIssue718() throws Exception {
        assertTrue(Transformer.transform("SELECT a FROM x WHERE a LIKE '%' + char(9) + '%'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTrueFalseLiteral() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM tbl WHERE true OR clm1 = 3", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTopKeyWord() throws Exception {
        assertTrue(Transformer.transform("SELECT top.date AS mycol1 FROM mytable top WHERE top.myid = :myid AND top.myid2 = 123", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTopKeyWord2() throws Exception {
        assertTrue(Transformer.transform("SELECT top.date", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTopKeyWord3() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable top", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotProblem1() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytab WHERE NOT v IN (1, 2, 3, 4, 5, 6, 7)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotProblem2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytab WHERE NOT func(5)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCaseThenCondition() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE CASE WHEN a = 'c' THEN a IN (1, 2, 3) END = 1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCaseThenCondition2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE CASE WHEN a = 'c' THEN a IN (1, 2, 3) END", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCaseThenCondition3() throws Exception {
        assertTrue(Transformer.transform("SELECT CASE WHEN a > 0 THEN b + a ELSE 0 END p FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCaseThenCondition4() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM col WHERE CASE WHEN a = 'c' THEN a IN (SELECT id FROM mytable) END", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCaseThenCondition5() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM col WHERE CASE WHEN a = 'c' THEN a IN (SELECT id FROM mytable) ELSE b IN (SELECT id FROM mytable) END", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOptimizeForIssue348() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM EMP ORDER BY SALARY DESC OPTIMIZE FOR 20 ROWS", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFuncConditionParameter() throws Exception {
        assertTrue(Transformer.transform("SELECT if(a < b)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFuncConditionParameter2() throws Exception {
        assertTrue(Transformer.transform("SELECT if(a < b, c)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFuncConditionParameter3() throws Exception {
    }

    @Test
    public void testFuncConditionParameter4() throws Exception {
    }

    @Test
    public void testSqlContainIsNullFunctionShouldBeParsed3() throws Exception {
        assertTrue(Transformer.transform("SELECT name, age FROM person WHERE NOT ISNULL(home, 'earn more money')", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testForXmlPath() throws Exception {
        assertTrue(Transformer.transform("SELECT '|' + person_name FROM person JOIN person_group ON person.person_id = person_group.person_id WHERE person_group.group_id = 1 FOR XML PATH('')", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testChainedunctions() throws Exception {
        assertTrue(Transformer.transform("SELECT func('').func2('') AS foo FROM some_tables", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCollateExprIssue164() throws Exception {
        assertTrue(Transformer.transform("SELECT u.name COLLATE Latin1_General_CI_AS AS User FROM users u", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotVariant() throws Exception {
        assertTrue(Transformer.transform("SELECT ! (1 + 1)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotVariant2() throws Exception {
        assertTrue(Transformer.transform("SELECT ! 1 + 1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotVariant3() throws Exception {
        assertTrue(Transformer.transform("SELECT NOT (1 + 1)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotVariant4() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE NOT (1 = 1)", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE ! (1 = 1)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotVariantIssue850() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE id = 1 AND ! (id = 1 AND id = 2)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDateArithmentic() throws Exception {
        assertTrue(Transformer.transform("SELECT CURRENT_DATE + (1 DAY) FROM SYSIBM.SYSDUMMY1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDateArithmentic2() throws Exception {
        assertTrue(Transformer.transform("SELECT CURRENT_DATE + 1 DAY AS NEXT_DATE FROM SYSIBM.SYSDUMMY1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDateArithmentic3() throws Exception {
        assertTrue(Transformer.transform("SELECT CURRENT_DATE + 1 DAY NEXT_DATE FROM SYSIBM.SYSDUMMY1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDateArithmentic4() throws Exception {
        assertTrue(Transformer.transform("SELECT CURRENT_DATE - 1 DAY + 1 YEAR - 1 MONTH FROM SYSIBM.SYSDUMMY1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDateArithmentic5() throws Exception {
        assertTrue(Transformer.transform("SELECT CASE WHEN CURRENT_DATE BETWEEN (CURRENT_DATE - 1 DAY) AND ('2019-01-01') THEN 1 ELSE 0 END FROM SYSIBM.SYSDUMMY1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDateArithmentic6() throws Exception {
        assertTrue(Transformer.transform("SELECT CURRENT_DATE + HOURS_OFFSET HOUR AS NEXT_DATE FROM SYSIBM.SYSDUMMY1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDateArithmentic7() throws Exception {
        assertTrue(Transformer.transform("SELECT CURRENT_DATE + MINUTE_OFFSET MINUTE AS NEXT_DATE FROM SYSIBM.SYSDUMMY1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDateArithmentic8() throws Exception {
        assertTrue(Transformer.transform("SELECT CURRENT_DATE + SECONDS_OFFSET SECOND AS NEXT_DATE FROM SYSIBM.SYSDUMMY1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNotProblemIssue721() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM dual WHERE NOT regexp_like('a', '[\\w]+')", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue699() throws Exception {
        assertTrue(Transformer.transform(" AND time < TIMESTAMP(DATE_SUB(CURDATE(),INTERVAL (2 - 1) DAY),'00:00:00')", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDateArithmentic9() throws Exception {
        assertTrue(Transformer.transform("SELECT CURRENT_DATE + (RAND() * 12 MONTH) AS new_date FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDateArithmentic10() throws Exception {
        assertTrue(Transformer.transform("String sql = select CURRENT_DATE + CASE WHEN CAST(RAND() * 3 AS INTEGER) = 1 THEN 100 ELSE 0 END DAY AS NEW_DATE from mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDateArithmentic11() throws Exception {
        assertTrue(Transformer.transform("String sql = select CURRENT_DATE + (dayofweek(MY_DUE_DATE) + 5) DAY FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDateArithmentic12() throws Exception {
    }

    @Test
    public void testDateArithmentic13() throws Exception {
        assertTrue(Transformer.transform("String sql = SELECT INTERVAL 5 MONTH MONTH FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testRawStringExpressionIssue656() throws Exception {
        assertTrue(Transformer.transform("String sql = select  + prefix + 'test' from foo", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testGroupingSets1() throws Exception {
    }

    @Test
    public void testGroupingSets2() throws Exception {
        assertTrue(Transformer.transform("SELECT COL_1 FROM TABLE_1 GROUP BY GROUPING SETS (COL_1)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testGroupingSets3() throws Exception {
        assertTrue(Transformer.transform("SELECT COL_1 FROM TABLE_1 GROUP BY GROUPING SETS (COL_1, ())", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLongQualifiedNamesIssue763() throws Exception {
        assertTrue(Transformer.transform("SELECT mongodb.test.test.intField, postgres.test.test.intField, postgres.test.test.datefield FROM mongodb.test.test JOIN postgres.postgres.test.test ON mongodb.test.test.intField = postgres.test.test.intField WHERE mongodb.test.test.intField = 123", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLongQualifiedNamesIssue763_2() throws Exception {
    }

    @Test
    public void testSubQueryAliasIssue754() throws Exception {
        assertTrue(Transformer.transform("SELECT C0 FROM T0 INNER JOIN T1 ON C1 = C0 INNER JOIN (SELECT W1 FROM T2) S1 ON S1.W1 = C0 ORDER BY C0", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSimilarToIssue789() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (w_id SIMILAR TO '/foo/__/bar/(left|right)/[0-9]{4}-[0-9]{2}-[0-9]{2}(/[0-9]*)?')", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSimilarToIssue789_2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (w_id NOT SIMILAR TO '/foo/__/bar/(left|right)/[0-9]{4}-[0-9]{2}-[0-9]{2}(/[0-9]*)?')", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCaseWhenExpressionIssue262() throws Exception {
        assertTrue(Transformer.transform("SELECT X1, (CASE WHEN T.ID IS NULL THEN CASE P.WEIGHT * SUM(T.QTY) WHEN 0 THEN NULL ELSE P.WEIGHT END ELSE SUM(T.QTY) END) AS W FROM A LEFT JOIN T ON T.ID = ? RIGHT JOIN P ON P.ID = ?", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCaseWhenExpressionIssue200() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM t1, t2 WHERE CASE WHEN t1.id = 1 THEN t2.name = 'Marry' WHEN t1.id = 2 THEN t2.age = 10 END", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeywordDuplicate() throws Exception {
        assertTrue(Transformer.transform("SELECT mytable.duplicate FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeywordDuplicate2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE duplicate = 5", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testEmptyDoubleQuotes() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE col = \\", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testEmptyDoubleQuotes_2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE col = \" \"", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testInnerWithBlock() throws Exception {
    }

    @Test
    public void testArrayIssue648() throws Exception {
    }

    @Test
    public void testArrayIssue638() throws Exception {
        assertTrue(Transformer.transform("SELECT PAYLOAD[0] FROM MYTABLE", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testArrayIssue489() throws Exception {
        assertTrue(Transformer.transform("SELECT name[1] FROM MYTABLE", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testArrayIssue377() throws Exception {
    }

    @Test
    public void testArrayIssue378() throws Exception {
    }

    @Test
    public void testArrayRange() throws Exception {
        assertTrue(Transformer.transform("SELECT (arr[1:3])[1] FROM MYTABLE", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue842() throws Exception {
    }

    @Test
    public void testIssue842_2() throws Exception {
        assertTrue(Transformer.transform("SELECT INTERVAL a.repayment_period DAY", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue848() throws Exception {
        assertTrue(Transformer.transform("SELECT IF(USER_ID > 10 AND SEX = 1, 1, 0)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue848_2() throws Exception {
        assertTrue(Transformer.transform("SELECT IF(USER_ID > 10, 1, 0)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue848_3() throws Exception {
        assertTrue(Transformer.transform("SELECT c1, multiset(SELECT * FROM mytable WHERE cond = 10) FROM T1 WHERE cond2 = 20", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue848_4() throws Exception {
    }

    @Test
    public void testMultiColumnAliasIssue849() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable AS mytab2(col1, col2)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiColumnAliasIssue849_2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM crosstab('select rowid, attribute, value from ct where attribute = ''att2'' or attribute = ''att3'' order by 1,2') AS ct(row_name text, category_1 text, category_2 text, category_3 text)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLimitClauseDroppedIssue845() throws Exception {
    }

    @Test
    public void testLimitClauseDroppedIssue845_2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM employee ORDER BY emp_id LIMIT 10 OFFSET 2", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testChangeKeywordIssue859() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM CHANGE.TEST", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testEndKeyword() throws Exception {
        assertTrue(Transformer.transform("SELECT end AS end_6 FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testStartKeyword() throws Exception {
        assertTrue(Transformer.transform("SELECT c0_.start AS start_5 FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSizeKeywordIssue867() throws Exception {
        assertTrue(Transformer.transform("SELECT size FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPartitionByWithBracketsIssue865() throws Exception {
        assertTrue(Transformer.transform("SELECT subject_id, student_id, sum(mark) OVER (PARTITION BY subject_id, student_id ) FROM marks", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT subject_id, student_id, sum(mark) OVER (PARTITION BY (subject_id, student_id) ) FROM marks", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWithAsRecursiveIssue874() throws Exception {
        assertTrue(Transformer.transform("WITH rn AS (SELECT rownum rn FROM dual CONNECT BY level <= (SELECT max(cases) FROM t1)) SELECT pname FROM t1, rn WHERE rn <= cases ORDER BY pname", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSessionKeywordIssue876() throws Exception {
        assertTrue(Transformer.transform("SELECT ID_COMPANY FROM SESSION.COMPANY", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWindowClauseWithoutOrderByIssue869() throws Exception {
        assertTrue(Transformer.transform("SELECT subject_id, student_id, mark, sum(mark) OVER (PARTITION BY (subject_id) ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) FROM marks", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeywordSizeIssue880() throws Exception {
    }

    @Test
    public void testKeywordCharacterIssue884() throws Exception {
        assertTrue(Transformer.transform("SELECT Character, Duration FROM actor", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCrossApplyIssue344() throws Exception {
    }

    @Test
    public void testOuterApplyIssue930() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable D OUTER APPLY (SELECT * FROM mytable2 E WHERE E.ColID = D.ColID) A", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testWrongParseTreeIssue89() throws Exception {
    }

    @Test
    public void testCaseWithComplexWhenExpression() throws Exception {
    }

    @Test
    public void testOrderKeywordIssue932() throws Exception {
        assertTrue(Transformer.transform("SELECT order FROM tmp3", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT tmp3.order FROM tmp3", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testOrderKeywordIssue932_2() throws Exception {
        assertTrue(Transformer.transform("SELECT group FROM tmp3", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT tmp3.group FROM tmp3", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTableFunctionInExprIssue923() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE func(a) IN func(b)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTableFunctionInExprIssue923_4() throws Exception {
    }

    @Test
    public void testTableFunctionInExprIssue923_5() throws Exception {
    }

    @Test
    public void testTableFunctionInExprIssue923_6() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE func(a) IN '1'", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeyWordCreateIssue941() throws Exception {
        assertTrue(Transformer.transform("SELECT b.create FROM table b WHERE b.id = 1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeyWordCreateIssue941_2() throws Exception {
    }

    @Test
    public void testCurrentIssue940() throws Exception {
        assertTrue(Transformer.transform("SELECT date(current) AS test_date FROM systables WHERE tabid = 1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeyWordView() throws Exception {
    }

    @Test
    public void testPreserveAndOperator() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE 1 = 2 && 2 = 3", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPreserveAndOperator_2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (field_1 && ?)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCheckDateFunctionIssue() throws Exception {
        assertTrue(Transformer.transform("SELECT DATEDIFF(NOW(), MIN(s.startTime))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCheckDateFunctionIssue_2() throws Exception {
        assertTrue(Transformer.transform("SELECT DATE_SUB(NOW(), INTERVAL :days DAY)", List.of(new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCheckDateFunctionIssue_3() throws Exception {
        assertTrue(Transformer.transform("SELECT DATE_SUB(NOW(), INTERVAL 1 DAY)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCheckColonVariable() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE (col1, col2) IN ((:qp0, :qp1), (:qp2, :qp3))", List.of(new SqlTypedParamValue("Integer", 10), new SqlTypedParamValue("Integer", 20), new SqlTypedParamValue("Integer", 30), new SqlTypedParamValue("Integer", 40))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testVariableAssignment() throws Exception {
        assertTrue(Transformer.transform("SELECT @SELECTVariable = 2", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testVariableAssignment2() throws Exception {
        assertTrue(Transformer.transform("SELECT @var = 1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testVariableAssignment3() throws Exception {
        assertTrue(Transformer.transform("SELECT @varname := @varname + 1 AS counter", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeyWordOfIssue1029() throws Exception {
        assertTrue(Transformer.transform("SELECT of.Full_Name_c AS FullName FROM comdb.Offer_c AS of", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeyWordExceptIssue1026() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM xxx WHERE exclude = 1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectConditionsIssue720And991() throws Exception {
        assertTrue(Transformer.transform("SELECT column IS NOT NULL FROM table", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT 0 IS NULL", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT 1 + 2", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT 1 < 2", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT 1 > 2", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT 1 + 2 AS a, 3 < 4 AS b", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT 1 < 2 AS a, 0 IS NULL AS b", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeyWordExceptIssue1040() throws Exception {
        assertTrue(Transformer.transform("SELECT FORMAT(100000, 2)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeyWordExceptIssue1044() throws Exception {
        assertTrue(Transformer.transform("SELECT SP_ID FROM ST_PR WHERE INSTR(',' || SP_OFF || ',', ',' || ? || ',') > 0", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeyWordExceptIssue1055() throws Exception {
        assertTrue(Transformer.transform("SELECT INTERVAL ? DAY", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeyWordExceptIssue1055_2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE A.end_time > now() AND A.end_time <= date_add(now(), INTERVAL ? DAY)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue1062() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE temperature.timestamp <= @to AND temperature.timestamp >= @from", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue1062_2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE temperature.timestamp <= @until AND temperature.timestamp >= @from", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIssue1068() throws Exception {
        assertTrue(Transformer.transform("SELECT t2.c AS div", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void selectWithSingleIn() throws Exception {
        assertTrue(Transformer.transform("SELECT 1 FROM dual WHERE a IN 1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeywordSequenceIssue1075() throws Exception {
        assertTrue(Transformer.transform("SELECT a.sequence FROM all_procedures a", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeywordSequenceIssue1074() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM t_user WITH (NOLOCK)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testContionItemsSelectedIssue1077() throws Exception {
        assertTrue(Transformer.transform("SELECT 1 > 0", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testExistsKeywordIssue1076() throws Exception {
        assertTrue(Transformer.transform("SELECT EXISTS (4)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testExistsKeywordIssue1076_1() throws Exception {
        assertTrue(Transformer.transform("SELECT mycol, EXISTS (SELECT mycol FROM mytable) mycol2 FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFormatKeywordIssue1078() throws Exception {
        assertTrue(Transformer.transform("SELECT FORMAT(date, 'yyyy-MM') AS year_month FROM mine_table", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testConditionalParametersForFunctions() throws Exception {
        assertTrue(Transformer.transform("SELECT myFunc(SELECT mycol FROM mytable)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCreateTableWithParameterDefaultFalseIssue1088() throws Exception {
        assertTrue(Transformer.transform("SELECT p.*, rhp.house_id FROM rel_house_person rhp INNER JOIN person p ON rhp.person_id = p.if WHERE rhp.house_id IN (SELECT house_id FROM rel_house_person WHERE person_id = :personId AND current_occupant = :current) AND rhp.current_occupant = :currentOccupant", List.of(new SqlTypedParamValue("Integer", 10), new SqlTypedParamValue("Integer", 10), new SqlTypedParamValue("Integer", 10))) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMissingLimitKeywordIssue1006() throws Exception {
    }

    @Test
    public void testKeywordUnsignedIssue961() throws Exception {
        assertTrue(Transformer.transform("SELECT COLUMN1, COLUMN2, CASE WHEN COLUMN1.DATA NOT IN ('1', '3') THEN CASE WHEN CAST(COLUMN2 AS UNSIGNED) IN ('1', '2', '3') THEN 'Q1' ELSE 'Q2' END END AS YEAR FROM TESTTABLE", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testH2CaseWhenFunctionIssue1091() throws Exception {
        assertTrue(Transformer.transform("SELECT CASEWHEN(ID = 1, 'A', 'B') FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMultiPartTypesIssue992() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST('*' AS pg_catalog.text)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSetOperationWithParenthesisIssue1094() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM ((SELECT A FROM tbl) UNION DISTINCT (SELECT B FROM tbl2)) AS union1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSetOperationWithParenthesisIssue1094_2() throws Exception {
    }

    @Test
    public void testSetOperationWithParenthesisIssue1094_3() throws Exception {
    }

    @Test
    public void testSetOperationWithParenthesisIssue1094_4() throws Exception {
    }

    @Test
    public void testSignedKeywordIssue1100() throws Exception {
        assertTrue(Transformer.transform("SELECT signed, unsigned FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSignedKeywordIssue995() throws Exception {
        assertTrue(Transformer.transform("SELECT leading FROM prd_reprint", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectTuple() throws Exception {
        assertTrue(Transformer.transform("SELECT hyperloglog_distinct((1, 2)) FROM t", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testArrayDeclare() throws Exception {
        assertTrue(Transformer.transform("SELECT ARRAY[1, f1], ARRAY[[1, 2], [3, f2 + 1]], ARRAY[]::text[] FROM t1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testColonDelimiterIssue1134() throws Exception {
    }

    @Test
    public void testKeywordSkipIssue1136() throws Exception {
        assertTrue(Transformer.transform("SELECT skip", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeywordAlgorithmIssue1137() throws Exception {
        assertTrue(Transformer.transform("SELECT algorithm FROM tablename", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeywordAlgorithmIssue1138() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM in.tablename", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFunctionOrderBy() throws Exception {
        assertTrue(Transformer.transform("SELECT array_agg(DISTINCT s ORDER BY b)[1] FROM t", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblematicDeparsingIssue1183() throws Exception {
        assertTrue(Transformer.transform("SELECT ARRAY_AGG(NAME ORDER BY ID) FILTER (WHERE NAME IS NOT NULL)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testProblematicDeparsingIssue1183_2() throws Exception {
        assertTrue(Transformer.transform("SELECT ARRAY_AGG(ID ORDER BY ID) OVER (ORDER BY ID)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testKeywordCostsIssue1185() throws Exception {
        assertTrue(Transformer.transform("WITH costs AS (SELECT * FROM MY_TABLE1 AS ALIAS_TABLE1) SELECT * FROM TESTSTMT", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testFunctionWithComplexParameters_Issue1190() throws Exception {
    }

    @Test
    public void testConditionsWithExtraBrackets_Issue1194() throws Exception {
    }

    @Test
    public void testWithValueListWithExtraBrackets1135() throws Exception {
    }

    @Test
    public void testWithValueListWithOutExtraBrackets1135() throws Exception {
    }

    @Test
    public void testWithInsideWithIssue1186() throws Exception {
    }

    @Test
    public void testKeywordSynonymIssue1211() throws Exception {
    }

    @Test
    public void testGroupedByIssue1176() throws Exception {
    }

    @Test
    public void testGroupedByWithExtraBracketsIssue1210() throws Exception {
    }

    @Test
    public void testGroupedByWithExtraBracketsIssue1168() throws Exception {
    }

    @Test
    public void testSelectRowElement() throws Exception {
        assertTrue(Transformer.transform("SELECT (t.tup).id, (tup).name FROM t WHERE (t.tup).id IN (1, 2, 3)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectCastProblemIssue1248() throws Exception {
        assertTrue(Transformer.transform("SELECT CAST(t1.sign2 AS Nullable (char))", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMissinBracketsNestedInIssue() throws Exception {
    }

    @Test
    public void testAnyComparisionExpressionValuesList1232() throws Exception {
    }

    @Test
    public void testSelectAllOperatorIssue1140() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM table t0 WHERE t0.id != all(5)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testSelectAllOperatorIssue1140_2() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM table t0 WHERE t0.id != all(?::uuid[])", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testDB2SpecialRegisterDateTimeIssue1249() throws Exception {
    }

    @Test
    public void testKeywordFilterIssue1255() throws Exception {
        assertTrue(Transformer.transform("SELECT col1 AS filter FROM table", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testConnectByRootIssue1255() throws Exception {
    }

    @Test
    public void testUnionLimitOrderByIssue1268() throws Exception {
        assertTrue(Transformer.transform("String sqlStr = (SELECT __time FROM traffic_protocol_stat_log LIMIT 1) UNION ALL (SELECT __time FROM traffic_protocol_stat_log ORDER BY __time LIMIT 1)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCastToRowConstructorIssue1267() throws Exception {
        assertTrue(Transformer.transform("String sqlStr = SELECT CAST(ROW(dataid, value, calcMark) AS ROW(datapointid CHAR, value CHAR, calcMark CHAR)) AS datapoints", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCollisionWithSpecialStringFunctionsIssue1284() throws Exception {
    }

    @Test
    public void testJoinWithTrailingOnExpressionIssue1302() throws Exception {
    }

    @Test
    public void testSimpleJoinOnExpressionIssue1229() throws Exception {
    }

    @Test
    public void testNestedCaseComplexExpressionIssue1306() throws Exception {
    }

    @Test
    public void testGroupByComplexExpressionIssue1308() throws Exception {
    }

    @Test
    public void testReservedKeywordsMSSQLUseIndexIssue1325() throws Exception {
    }

    @Test
    public void testReservedKeywordsIssue1352() throws Exception {
    }

    @Test
    public void testTableSpaceKeyword() throws Exception {
    }

    @Test
    public void testTableSpecificAllColumnsIssue1346() throws Exception {
    }

    @Test
    public void testPostgresDollarQuotes_1372() throws Exception {
        assertTrue(Transformer.transform("SELECT UPPER($$some text$$) FROM a", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM a WHERE a.test = $$where text$$", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM a WHERE a.test = $$$$", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM a WHERE a.test = $$ $$", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT aa AS $$My Column Name$$ FROM a", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testCanCallSubSelectOnWithItemEvenIfNotSetIssue1369() throws Exception {
    }

    @Test
    public void testCaseElseExpressionIssue1375() throws Exception {
    }

    @Test
    public void testComplexInExpressionIssue905() throws Exception {
    }

    @Test
    public void testLogicalExpressionSelectItemIssue1381() throws Exception {
    }

    @Test
    public void testKeywordAtIssue1414() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM table1 at", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testIgnoreNullsForWindowFunctionsIssue1429() throws Exception {
        assertTrue(Transformer.transform("SELECT lag(mydata) IGNORE NULLS OVER (ORDER BY sortorder) AS previous_status FROM mytable", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPerformanceIssue1438() throws Exception {
    }

    @Test
    public void testPerformanceIssue1397() throws Exception {
    }

    @Test
    public void testParserInterruptedByTimeout() throws Exception {
        assertTrue(Transformer.transform(" \tt1.id ASC", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void run() throws Exception {
    }

    @Test
    public void execute() throws Throwable {
    }

    @Test
    public void testWithIsolation() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 WITH ur", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT * FROM mytable WHERE mytable.col = 9 WITH Cs", List.of()) instanceof SearchSourceBuilder);
        assertTrue(Transformer.transform("SELECT rs.col, * FROM mytable RS WHERE mytable.col = 9", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testLoclTimezone1471() throws Exception {
        assertTrue(Transformer.transform("SELECT TO_CHAR(CAST(SYSDATE AS TIMESTAMP WITH LOCAL TIME ZONE), 'HH:MI:SS AM TZD') FROM DUAL", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testMissingLimitIssue1505() throws Exception {
        assertTrue(Transformer.transform("(SELECT * FROM mytable) LIMIT 1", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testPostgresNaturalJoinIssue1559() throws Exception {
    }

    @Test
    public void testNamedWindowDefinitionIssue1581() throws Exception {
        assertTrue(Transformer.transform("SELECT sum(salary) OVER w, avg(salary) OVER w FROM empsalary WINDOW w AS (PARTITION BY depname ORDER BY salary DESC)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testNamedWindowDefinitionIssue1581_2() throws Exception {
        assertTrue(Transformer.transform("SELECT sum(salary) OVER w1, avg(salary) OVER w2 FROM empsalary WINDOW w1 AS (PARTITION BY depname ORDER BY salary DESC), w2 AS (PARTITION BY depname2 ORDER BY salary2)", List.of()) instanceof SearchSourceBuilder);
    }

    @Test
    public void testTimestamptzDateTimeLiteral() throws Exception {
        assertTrue(Transformer.transform("SELECT * FROM table WHERE x >= TIMESTAMPTZ '2021-07-05 00:00:00+00'", List.of()) instanceof SearchSourceBuilder);
    }

    /**
     * Test of transform method, of class Transformer.
     */
    @org.junit.Test
    public void testTransform() throws Exception {
        System.out.println("transform");
        String sql = "";
        List<SqlTypedParamValue> params = List.of();
        SearchSourceBuilder expResult = null;
        SearchSourceBuilder result = Transformer.transform(sql, params);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of order method, of class Transformer.
     */
    @org.junit.Test
    public void testOrder() {
        System.out.println("order");
        List<OrderByElement> orderByElements = null;
        Transformer instance = new Transformer();
        instance.order(orderByElements);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of sqlSelectQueryToElasticSearchQuery method, of class Transformer.
     */
    @org.junit.Test
    public void testSqlSelectQueryToElasticSearchQuery() throws Exception {
        System.out.println("sqlSelectQueryToElasticSearchQuery");
        String sql = "";
        List<SqlTypedParamValue> params = null;
        Transformer instance = new Transformer();
        SearchSourceBuilder expResult = null;
        SearchSourceBuilder result = instance.sqlSelectQueryToElasticSearchQuery(sql, params);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
