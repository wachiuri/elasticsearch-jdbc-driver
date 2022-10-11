/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.querybuilders;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsComparisonOperator;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 *
 * @author Timothy Wachiuri
 */
public class EsBoolQueryBuilder extends EsBinaryExpression {

    private String operator;

    public EsBoolQueryBuilder(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public Object toObject() {

        System.out.println("EsBoolQueryBuilder.toObject() left Expression "
                + this.getLeftExpression()
                + " string expression " + this.getOperator()
                + " right expression " + this.getRightExpression());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        switch (getOperator()) {
            case "AND":
                boolQueryBuilder.must().add((QueryBuilder) getLeftExpression().toObject());
                boolQueryBuilder.must().add((QueryBuilder) getRightExpression().toObject());
                break;
            case "OR":
                boolQueryBuilder.should().add((QueryBuilder) getLeftExpression().toObject());
                boolQueryBuilder.should().add((QueryBuilder) getRightExpression().toObject());
                break;
            case "NOT":
                boolQueryBuilder.mustNot((QueryBuilder) getLeftExpression().toObject());

                if (getRightExpression() != null) {
                    boolQueryBuilder.mustNot((QueryBuilder) getRightExpression().toObject());
                }
                break;

            default:
                throw new UnsupportedOperationException("EsBoolQueryBuilder.toObject operator " + getOperator() + " NOT executed");
        }

        return boolQueryBuilder;
    }

    @Override
    public String getStringExpression() {
        return operator;
    }

}
