/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.querybuilders;

import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import org.elasticsearch.index.query.QueryBuilders;

/**
 *
 * @author Timothy Wachiuri
 */
public class EsTermQueryBuilder extends EsBinaryExpression {

    private final String operator="=";

    public EsTermQueryBuilder() {
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String getStringExpression() {
        return operator;
    }

    @Override
    public Object toObject() {
        return QueryBuilders.termQuery(this.getLeftExpression().toString(), this.getRightExpression().toObject());
    }

}
