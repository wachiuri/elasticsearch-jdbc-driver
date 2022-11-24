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

public class EsGreaterThan extends EsComparisonOperator implements EsQueryBuilder {

    public EsGreaterThan() {
        super(">");
    }

    @Override
    public EsGreaterThan withLeftExpression(Branch arg0) {
        return (EsGreaterThan) super.withLeftExpression(arg0);
    }

    @Override
    public EsGreaterThan withRightExpression(Branch arg0) {
        return (EsGreaterThan) super.withRightExpression(arg0);
    }

    @Override
    public QueryBuilder toQueryBuilder() {
        return QueryBuilders.rangeQuery(getLeftExpression().toString())
                .gt(getRightExpression().toObject());
    }
}
