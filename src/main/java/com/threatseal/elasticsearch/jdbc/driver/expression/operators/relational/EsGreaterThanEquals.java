/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.querybuilders.EsQueryBuilder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

public class EsGreaterThanEquals extends EsComparisonOperator implements EsQueryBuilder  {

    private String timeZone;

    public EsGreaterThanEquals() {
        super(">=");
    }

    public EsGreaterThanEquals(String operator) {
        super(operator);
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public EsGreaterThanEquals withLeftExpression(Branch arg0) {
        return (EsGreaterThanEquals) super.withLeftExpression(arg0);
    }

    @Override
    public EsGreaterThanEquals withRightExpression(Branch arg0) {
        return (EsGreaterThanEquals) super.withRightExpression(arg0);
    }

    public EsGreaterThanEquals withTimeZone(String timeZone){
        this.timeZone = timeZone;
        return this;
    }
    
    @Override
    public QueryBuilder toQueryBuilder() {

        RangeQueryBuilder rangeQueryBuilder=
         QueryBuilders.rangeQuery(getLeftExpression().toString())
                .gte(getRightExpression().toObject());

        if(!timeZone.isEmpty()){
            rangeQueryBuilder.timeZone(timeZone);
        }

        return rangeQueryBuilder;
    }
}
