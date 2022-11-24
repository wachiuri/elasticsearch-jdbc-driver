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

public class EsMinorThanEquals extends EsComparisonOperator implements EsQueryBuilder{

    public EsMinorThanEquals() {
        super("<=");
    }

    public EsMinorThanEquals(String operator) {
        super(operator);
    }

    @Override
    public EsMinorThanEquals withLeftExpression(Branch arg0) {
        return (EsMinorThanEquals) super.withLeftExpression(arg0);
    }

    @Override
    public EsMinorThanEquals withRightExpression(Branch arg0) {
        return (EsMinorThanEquals) super.withRightExpression(arg0);
    }

    @Override
    public QueryBuilder toQueryBuilder() {
        return QueryBuilders.rangeQuery(getLeftExpression().toString())
                .lte(getRightExpression().toObject());
    }
}
