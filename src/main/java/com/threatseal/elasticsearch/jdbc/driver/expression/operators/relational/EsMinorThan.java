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

public class EsMinorThan extends EsComparisonOperator implements EsQueryBuilder{

    public EsMinorThan() {
        super("<");
    }

    @Override
    public EsMinorThan withLeftExpression(Branch arg0) {
        return (EsMinorThan) super.withLeftExpression(arg0);
    }

    @Override
    public EsMinorThan withRightExpression(Branch arg0) {
        return (EsMinorThan) super.withRightExpression(arg0);
    }

    @Override
    public QueryBuilder toQueryBuilder() {
        return QueryBuilders.rangeQuery(getLeftExpression().toString())
                .lt(getRightExpression().toObject());
    }
}
