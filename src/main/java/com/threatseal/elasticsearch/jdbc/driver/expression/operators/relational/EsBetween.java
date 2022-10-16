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
import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import com.threatseal.elasticsearch.jdbc.driver.querybuilders.EsQueryBuilder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

/**
 * A "BETWEEN" expr1 expr2 statement
 */
public class EsBetween extends BranchImpl implements EsQueryBuilder {

    private Branch leftExpression;
    private boolean not = false;
    private Branch betweenExpressionStart;
    private Branch betweenExpressionEnd;

    public Branch getBetweenExpressionEnd() {
        return betweenExpressionEnd;
    }

    public Branch getBetweenExpressionStart() {
        return betweenExpressionStart;
    }

    public Branch getLeftExpression() {
        return leftExpression;
    }

    public boolean isNot() {
        return not;
    }

    public void setBetweenExpressionEnd(Branch expression) {
        betweenExpressionEnd = expression;
    }

    public void setBetweenExpressionStart(Branch expression) {
        betweenExpressionStart = expression;
    }

    public void setLeftExpression(Branch expression) {
        leftExpression = expression;
    }

    public void setNot(boolean b) {
        not = b;
    }

    //@Override
    public void accept(ExpressionVisitor expressionVisitor) {
        //expressionVisitor.visit(this);
    }

    @Override
    public String toString() {
        return leftExpression + " " + (not ? "NOT " : "") + "BETWEEN " + betweenExpressionStart + " AND "
                + betweenExpressionEnd;
    }

    @Override
    public Object toObject() {
        return toString();
    }

    public EsBetween withLeftExpression(Branch leftExpression) {
        this.setLeftExpression(leftExpression);
        return this;
    }

    public EsBetween withNot(boolean not) {
        this.setNot(not);
        return this;
    }

    public EsBetween withBetweenExpressionStart(Branch betweenExpressionStart) {
        this.setBetweenExpressionStart(betweenExpressionStart);
        return this;
    }

    public EsBetween withBetweenExpressionEnd(Branch betweenExpressionEnd) {
        this.setBetweenExpressionEnd(betweenExpressionEnd);
        return this;
    }

    public <E extends Expression> E getBetweenExpressionEnd(Class<E> type) {
        return type.cast(getBetweenExpressionEnd());
    }

    public <E extends Expression> E getBetweenExpressionStart(Class<E> type) {
        return type.cast(getBetweenExpressionStart());
    }

    public <E extends Expression> E getLeftExpression(Class<E> type) {
        return type.cast(getLeftExpression());
    }

    @Override
    public QueryBuilder toQueryBuilder() {

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(getLeftExpression().toString());

        rangeQueryBuilder.gte(getBetweenExpressionStart().toObject())
                .lte(getBetweenExpressionEnd().toObject());

        if (isNot()) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                    .mustNot(rangeQueryBuilder);

            return boolQueryBuilder;
        }
        return rangeQueryBuilder;
    }

}
