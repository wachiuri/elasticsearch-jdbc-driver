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
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import com.threatseal.elasticsearch.jdbc.driver.querybuilders.EsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class EsIsBooleanExpression extends BranchImpl implements EsQueryBuilder {

    private Branch leftExpression;
    private boolean not = false;
    private boolean isTrue = false;

    public Branch getLeftExpression() {
        return leftExpression;
    }

    public boolean isNot() {
        return not;
    }

    public void setLeftExpression(Branch expression) {
        leftExpression = expression;
    }

    public void setNot(boolean b) {
        not = b;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setIsTrue(boolean isTrue) {
        this.isTrue = isTrue;
    }

    @Override
    public String toString() {
        if (isTrue()) {
            return leftExpression + " IS" + (not ? " NOT" : "") + " TRUE";
        } else {
            return leftExpression + " IS" + (not ? " NOT" : "") + " FALSE";
        }
    }

    public EsIsBooleanExpression withIsTrue(boolean isTrue) {
        this.setIsTrue(isTrue);
        return this;
    }

    public EsIsBooleanExpression withLeftExpression(Branch leftExpression) {
        this.setLeftExpression(leftExpression);
        return this;
    }

    public EsIsBooleanExpression withNot(boolean not) {
        this.setNot(not);
        return this;
    }

    public <E extends Branch> E getLeftExpression(Class<E> type) {
        return type.cast(getLeftExpression());
    }

    @Override
    public QueryBuilder toQueryBuilder() {

        return QueryBuilders.termQuery(getLeftExpression().toString(), isTrue());

    }
}
