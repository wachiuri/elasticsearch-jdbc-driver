/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.transformer;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.proto.SqlTypedParamValue;
import com.threatseal.elasticsearch.jdbc.driver.querybuilders.EsQueryBuilder;
import com.threatseal.elasticsearch.jdbc.driver.schema.EsColumn;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.GroupByElement;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.OrderByVisitorAdapter;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.GroupByVisitor;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 *
 * @author Timothy Wachiuri
 */
public class Transformer {

    private static final Logger logger = Logger.getLogger(Transformer.class.getName());

    private final SearchSourceBuilder sourceBuilder;

    private final List<String> csvList = new ArrayList<>();

    public Transformer() {
        sourceBuilder = new SearchSourceBuilder();
    }

    public static SearchSourceBuilder transform(String sql, List<SqlTypedParamValue> params) throws JSQLParserException {

        Transformer transformer = new Transformer();

        return transformer.sqlSelectQueryToElasticSearchQuery(sql, params);
    }

    protected void groupBy(GroupByElement groupByElement) {

        try {
            //logger.log(Level.INFO,"grouping sets " + groupByElement.getGroupingSets());

            for (Expression term : groupByElement.getGroupByExpressionList().getExpressions()) {

                String termString = term.toString().replace("`", "");
                //logger.log(Level.INFO,"GROUP BY term " + term);
                if (termString.equals("timestamp")) {
                    sourceBuilder.aggregation(AggregationBuilders.dateHistogram("dateHistogram")
                            .field("timestamp")
                            .dateHistogramInterval(DateHistogramInterval.HOUR)
                            .interval(1)
                            .format("yyyy-MM-dd HH")
                            .order(Histogram.Order.COUNT_DESC)
                    );
                } else {
                    EsExpressionVisitorAdapter eeva = new EsExpressionVisitorAdapter(SQLStatementSection.GROUPBY);
                    term.accept(eeva);
                    logger.log(Level.INFO, "group by stack {0}", eeva.getStack());
                    logger.log(Level.INFO, "toObject {0}", eeva.getStack().peek().toObject());

                    sourceBuilder.aggregation(AggregationBuilders
                            .terms(termString)
                            .field(termString)
                            .size(sourceBuilder.size() >= 0 ? sourceBuilder.size() : 10)
                            .order(Terms.Order.count(false))
                    );
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "GROUP BY exception {0}", e.getMessage());
            //e.printStackTrace(System.out);
        }
    }

    protected void where(Expression whereExpression) {

        if (whereExpression == null) {
            sourceBuilder.query(QueryBuilders.matchAllQuery());
            return;
        }

        EsExpressionVisitorAdapter eeva = new EsExpressionVisitorAdapter(SQLStatementSection.WHERE);
        try {
            whereExpression.accept(eeva);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "where accept exception {0}", e.getMessage());
            //e.printStackTrace(System.out);
        }
        //logger.log(Level.INFO,"query builder " + queryBuilder);
        //logger.log(Level.INFO,"stack " + eeva.getStack());

        csvList.addAll(eeva.getList());

        logger.log(Level.INFO, "stack size {0}", eeva.getStack().size());

        eeva.getStack().forEach(action -> {
            logger.log(Level.INFO, "{0}:{1},", new Object[]{action, action.getClass().getSimpleName()});
        });

        if (eeva.getStack().size() > 1) {
            throw new IllegalStateException("Stack has more than one item");
        }

        Branch object = eeva.getStack().pop();

        if (object instanceof EsQueryBuilder) {
            QueryBuilder queryBuilder = ((EsQueryBuilder) object).toQueryBuilder();

            sourceBuilder.query(queryBuilder);
            logger.log(Level.INFO, "query builder {0}", sourceBuilder.query());
        } else {
            logger.log(Level.INFO, "ERROR : where result is not a query builder");
        }
    }

    protected void order(List<OrderByElement> orderByElements) {

        try {
            for (OrderByElement orderByElement : orderByElements) {
                orderByElement.accept(new OrderByVisitorAdapter() {
                    @Override
                    public void visit(OrderByElement orderBy) {

                        csvList.add("\"" + orderBy.getExpression().toString() + "\"");

                        logger.log(Level.FINER, "order by ", orderBy);
                        logger.log(Level.FINER, "order by expression ", orderBy.getExpression());

                        if (!orderBy.isAscDescPresent()) {
                            sourceBuilder.sort(orderBy.getExpression().toString().replace("`", ""));
                        } else if (orderBy.isAsc()) {
                            sourceBuilder.sort(orderBy.getExpression().toString().replace("`", ""), SortOrder.ASC);
                        } else {
                            sourceBuilder.sort(orderBy.getExpression().toString().replace("`", ""), SortOrder.DESC);
                        }

                    }

                });
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "order by exception {0}", e.getMessage());
            //e.printStackTrace(System.out);
        }

    }

    public SearchSourceBuilder sqlSelectQueryToElasticSearchQuery(String sql, List<SqlTypedParamValue> params) throws JSQLParserException {
        
        String paramString;
        for (SqlTypedParamValue param : params) {
            paramString = "";
            int parameterIndex = sql.indexOf("?");
            if (parameterIndex == -1) {
                break;
            }

            logger.log(Level.INFO, "param type {0}", param.type);
            logger.log(Level.INFO, "param value {0}", param.value);
            logger.log(Level.INFO, "param toString() {0}", param.toString());
            switch (param.type) {
                case "STRING":
                case "KEYWORD":
                    paramString = (String) param.value;
                    break;
                case "DATETIME":
                    {
                        Date date = (Date) param.value;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        paramString = sdf.format(date);
                        break;
                    }
                case "DATE":
                    {
                        Date date = (Date) param.value;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        paramString = sdf.format(date);
                        if (paramString != null) {
                            paramString = paramString.replace("00:00:00", "");
                        }       break;
                    }
                default:
                    logger.log(Level.SEVERE, "param type not evaluated {0}", param.type);
                    paramString = (String) param.value;
                    break;
            }

            sql = sql.substring(0, parameterIndex).concat("'").concat(paramString)
                    .concat("'").concat(sql.substring(parameterIndex + 1, sql.length()));
        }
        sql = sql.replace("\n", " ");

        logger.log(Level.INFO, "parsing {0}", sql);
        Statement stmt = CCJSqlParserUtil.parse(sql);

        EsStatementVisitorAdapter statementVisitorAdapter = new EsStatementVisitorAdapter();

        stmt.accept(statementVisitorAdapter);

        Select select = statementVisitorAdapter.getSelect();

        PlainSelect selectStatement = (PlainSelect) select.getSelectBody();

        List<OrderByElement> orderByElements = selectStatement.getOrderByElements();

        //logger.log(Level.INFO,"order by  elements " + orderByElements);
        if (orderByElements != null && !orderByElements.isEmpty()) {
            order(orderByElements);
        }

        Limit limit = selectStatement.getLimit();
        //logger.log(Level.INFO,"limit " + limit);
        if (limit != null) {
            sourceBuilder.size(Integer.parseInt(limit.getRowCount().toString()));

            if (limit.getOffset() != null) {
                sourceBuilder.from(Integer.parseInt(limit.getOffset().toString()));
            }
        }

        Expression where = selectStatement.getWhere();
        //logger.log(Level.INFO,"where expression " + where);

        where(where);

        GroupByElement groupByElement = selectStatement.getGroupBy();
        //logger.log(Level.INFO,"group by element " + groupByElement);
        if (groupByElement != null) {
            groupBy(groupByElement);
        }

        if (sourceBuilder.aggregations() != null && !sourceBuilder.aggregations().getAggregatorFactories().isEmpty()) {
            sourceBuilder.size(0);
        }

        for (SelectItem selectItem : selectStatement.getSelectItems()) {
            if (selectItem instanceof SelectExpressionItem) {
                SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
                logger.log(Level.INFO, "select expression item expression {0}", selectExpressionItem.getExpression().toString());

                if (selectExpressionItem.getAlias() != null) {
                    logger.log(Level.INFO, " alias {0}", selectExpressionItem.getAlias().getName());
                }
            } else if (selectItem instanceof AllColumns) {
                AllColumns allColumns = (AllColumns) selectItem;
                logger.log(Level.INFO, "all columns {0}", allColumns.toString());
            } else if (selectItem instanceof AllTableColumns) {
                AllTableColumns allTableColumns = (AllTableColumns) selectItem;
                logger.log(Level.INFO, "all table columns {0}", allTableColumns.toString());
            }
        }

        sourceBuilder.fetchSource(true);

        sourceBuilder.scriptField("username",
                new Script("doc['Message.username'].value!=null?doc['Message.username'].value"
                        + ":doc['TargetUsername.keyword'].value!=null?doc['TargetUsername.keyword'].value:\"\""),
                true);
        sourceBuilder.scriptField("ipaddress",
                new Script("doc['Message.ipaddress'].value!=null?doc['Message.ipaddress'].value"
                        + ":doc['ip_src_addr.keyword'].value!=null?doc['ip_src_addr.keyword'].value:\"\""),
                true);

        sourceBuilder
                .docValueField("Message.username")
                .docValueField("Message.ipaddress");

        logger.log(Level.INFO, "source builder {0}", sourceBuilder);

        if (sql.contains("\"")) {
//            logger.log(Level.INFO,"sql " + sql);
            sql = sql.replace("\"", "\\\"");
//            logger.log(Level.INFO,"new sql " + sql);
        }
        csvList.add(0, "\"" + new String(sql.getBytes(), StandardCharsets.UTF_8) + "\"");

        for (int i = csvList.size(); i <= 46; i++) {
            csvList.add("\"\"");
        }

        try {

            File file = new File("./queriesAnalysis.csv");
            FileWriter fileWriter = new FileWriter(file, true);

            fileWriter.write(String.join(",", csvList) + "\r\n");

            fileWriter.close();

            File noOfColumnsFile = new File("./noOfColumns.csv");
            FileWriter noOfColumns = new FileWriter(noOfColumnsFile, true);

            noOfColumns.write(csvList.size() + "," + sql.length() + "\r\n");

            noOfColumns.close();

        } catch (IOException ex) {
            Logger.getLogger(Transformer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sourceBuilder;

    }
}
