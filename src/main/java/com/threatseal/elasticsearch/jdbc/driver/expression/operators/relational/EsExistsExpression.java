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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class EsExistsExpression extends BranchImpl implements EsQueryBuilder {

    private Branch rightExpression;
    private boolean not = false;

    public Branch getRightExpression() {
        return rightExpression;
    }

    public void setRightExpression(Branch expression) {
        rightExpression = expression;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean b) {
        not = b;
    }

    public String getStringExpression() {
        return (not ? "NOT " : "") + "EXISTS";
    }

    @Override
    public String toString() {
        return getStringExpression() + " " + rightExpression.toString();
    }

    public EsExistsExpression withRightExpression(Branch rightExpression) {
        this.setRightExpression(rightExpression);
        return this;
    }

    public EsExistsExpression withNot(boolean not) {
        this.setNot(not);
        return this;
    }

    public <E extends Branch> E getRightExpression(Class<E> type) {
        return type.cast(getRightExpression());
    }

    @Override
    public QueryBuilder toQueryBuilder() {

        if (not) {
            return QueryBuilders.boolQuery().mustNot(
                    QueryBuilders.existsQuery(getRightExpression().toString())
            );
        }

        return QueryBuilders.existsQuery(getRightExpression().toString());
    }
}