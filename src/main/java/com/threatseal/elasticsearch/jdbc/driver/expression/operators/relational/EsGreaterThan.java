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

import java.util.UUID;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

public class EsGreaterThan extends EsComparisonOperator implements EsQueryBuilder {

    private String timeZone;

    public EsGreaterThan() {
        super(">");
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public EsGreaterThan withLeftExpression(Branch arg0) {
        return (EsGreaterThan) super.withLeftExpression(arg0);
    }

    @Override
    public EsGreaterThan withRightExpression(Branch arg0) {
        return (EsGreaterThan) super.withRightExpression(arg0);
    }

    public EsGreaterThan withTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    @Override
    public QueryBuilder toQueryBuilder() {

        RangeQueryBuilder rangeQueryBuilder =
                QueryBuilders.rangeQuery(getLeftExpression().toString())
                        .gt(getRightExpression().toObject());

        if (!this.timeZone.isEmpty()) {
            rangeQueryBuilder.timeZone(this.timeZone);
        }

        return rangeQueryBuilder;
    }
}
