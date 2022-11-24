/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression.operators.conditional;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import com.threatseal.elasticsearch.jdbc.driver.querybuilders.EsCompoundQueryBuilder;
import com.threatseal.elasticsearch.jdbc.driver.querybuilders.EsQueryBuilder;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class EsOrExpression extends EsConditionalOperator implements EsCompoundQueryBuilder {

    public EsOrExpression() {
        // nothing
    }

    public EsOrExpression(Branch leftExpression, Branch rightExpression) {
        setLeftExpression(leftExpression);
        setRightExpression(rightExpression);
    }

    @Override
    public EsOrExpression withLeftExpression(Branch expression) {
        return (EsOrExpression) super.withLeftExpression(expression);
    }

    @Override
    public EsOrExpression withRightExpression(Branch expression) {
        return (EsOrExpression) super.withRightExpression(expression);
    }

    @Override
    public String getStringExpression() {
        return "OR";
    }

    @Override
    public QueryBuilder toQueryBuilder() {
        if (!(getRightExpression() instanceof EsQueryBuilder)) {
            throw new IllegalStateException("Right expression "
                    + getRightExpression().getClass().getName()
                    + " is not a query builder");
        }

        if (getLeftExpression() instanceof EsQueryBuilder == false) {
            throw new IllegalStateException("Left expression "
                    + getLeftExpression().getClass().getName()
                    + " is not a query builder");
        }

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        queryBuilder.should(((EsQueryBuilder) this.getRightExpression()).toQueryBuilder());
        queryBuilder.should(((EsQueryBuilder) this.getLeftExpression()).toQueryBuilder());

        return queryBuilder;
    }

}
