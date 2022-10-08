/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.querybuilders;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsComparisonOperator;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 *
 * @author Timothy Wachiuri
 */
public class EsBoolQueryBuilder extends EsComparisonOperator {

    public EsBoolQueryBuilder(String operator) {
        super(operator);
    }

    @Override
    public Object toObject() {

        System.out.println("EsBoolQueryBuilder.toObject() left Expression " + this.getLeftExpression()
                + " string expression " + this.getOperator()
                + " right expression " + this.getRightExpression());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        switch (getOperator()) {
            case "=":
                boolQueryBuilder.must().add(QueryBuilders.matchQuery(this.getLeftExpression().toString(), this.getRightExpression()));
                break;

        }

        return boolQueryBuilder;
    }

}
