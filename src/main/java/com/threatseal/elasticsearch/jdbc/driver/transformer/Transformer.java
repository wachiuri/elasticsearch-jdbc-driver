/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.transformer;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.proto.SqlTypedParamValue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.GroupByElement;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.OrderByVisitorAdapter;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 *
 * @author Timothy Wachiuri
 */
public class Transformer {

    private static final Logger logger = Logger.getLogger(Transformer.class.getName());

    private final SearchSourceBuilder sourceBuilder;

    private List<String> csvList = new ArrayList<>();

    public Transformer() {
        sourceBuilder = new SearchSourceBuilder();
    }

    public static SearchSourceBuilder transform(String sql, List<SqlTypedParamValue> params) throws JSQLParserException {

        Transformer transformer = new Transformer();

        return transformer.sqlSelectQueryToElasticSearchQuery(sql, params);
    }

    protected void groupBy(GroupByElement groupByElement) {

        try {
            //System.out.println("grouping sets " + groupByElement.getGroupingSets());
            EsExpressionVisitorAdapter visitorAdapter = new EsExpressionVisitorAdapter(SQLStatementSection.GROUPBY);
            groupByElement.getGroupByExpressionList().accept(visitorAdapter);

            System.out.println(visitorAdapter.getStack());

            while (!visitorAdapter.getStack().empty()) {
                String term = visitorAdapter.getStack().pop().toString();
                //System.out.println("GROUP BY term " + term);
                sourceBuilder.aggregation(AggregationBuilders
                        .terms(term)
                        .field(term)
                );
            }
        } catch (Exception e) {
            System.out.println("GROUP BY  exception " + e.getMessage());
            e.printStackTrace(System.out);
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
            System.out.println("where accept exception " + e.getMessage());
            e.printStackTrace(System.out);
        }
        //System.out.println("query builder " + queryBuilder);
        //System.out.println("stack " + eeva.getStack());

        csvList.addAll(eeva.getList());

        System.out.println("stack size " + eeva.getStack().size());

        Branch object = eeva.getStack().pop();
        System.out.println(object);

        QueryBuilders.boolQuery();

        if (object.toObject() instanceof QueryBuilder) {
            QueryBuilder queryBuilder = (QueryBuilder) object.toObject();
            sourceBuilder.query(queryBuilder);
            System.out.println("query builder " + sourceBuilder.query());
        } else {
            System.out.println("ERROR : where result is not a query builder");
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
                            sourceBuilder.sort(orderBy.getExpression().toString());
                        } else if (orderBy.isAsc()) {
                            sourceBuilder.sort(orderBy.getExpression().toString(), SortOrder.ASC);
                        } else {
                            sourceBuilder.sort(orderBy.getExpression().toString(), SortOrder.DESC);
                        }

                    }

                });
            }

        } catch (Exception e) {
            System.out.println("order by exception " + e.getMessage());
            e.printStackTrace(System.out);
        }

    }

    public SearchSourceBuilder sqlSelectQueryToElasticSearchQuery(String sql, List<SqlTypedParamValue> params) throws JSQLParserException {
        //System.out.println("Transformer.sqlQueryToElasticSearchQuery sql " + sql + " params " + params);
        
        sql=sql.replace("\n", " ");
        Select stmt = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect selectStatement = (PlainSelect) stmt.getSelectBody();

        List<OrderByElement> orderByElements = selectStatement.getOrderByElements();

        //System.out.println("order by  elements " + orderByElements);
        if (orderByElements != null && !orderByElements.isEmpty()) {
            order(orderByElements);
        }

        Limit limit = selectStatement.getLimit();
        //System.out.println("limit " + limit);
        if (limit != null) {
            sourceBuilder.size(Integer.parseInt(limit.getRowCount().toString()));

            if (limit.getOffset() != null) {
                sourceBuilder.from(Integer.parseInt(limit.getOffset().toString()));
            }
        }

        Expression where = selectStatement.getWhere();
        //System.out.println("where expression " + where);

        where(where);

        GroupByElement groupByElement = selectStatement.getGroupBy();
        //System.out.println("group by element " + groupByElement);
        if (groupByElement != null) {
            groupBy(groupByElement);
        }

        if (sql.contains("\"")) {
//            System.out.println("sql " + sql);
            sql = sql.replace("\"", "\\\"");
//            System.out.println("new sql " + sql);
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
