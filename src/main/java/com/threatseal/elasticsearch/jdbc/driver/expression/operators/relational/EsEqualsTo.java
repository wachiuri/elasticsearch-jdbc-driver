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
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

public class EsEqualsTo extends EsComparisonOperator implements EsQueryBuilder {

    public EsEqualsTo() {
        super("=");
    }

    public EsEqualsTo(Branch left, Branch right) {
        this();
        setLeftExpression(left);
        setRightExpression(right);
    }

    @Override
    public EsEqualsTo withLeftExpression(Branch expression) {
        return (EsEqualsTo) super.withLeftExpression(expression);
    }

    @Override
    public EsEqualsTo withRightExpression(Branch expression) {
        return (EsEqualsTo) super.withRightExpression(expression);
    }

    @Override
    public TermQueryBuilder toQueryBuilder() {

        return QueryBuilders.termQuery(getLeftExpression().toString(), getRightExpression().toObject());
    }

}
