/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression;

import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

/**
 * A basic class for binary expressions, that is expressions having a left member and a right member
 * which are in turn expressions.
 */
public abstract class EsBinaryExpression extends BranchImpl {

    private Branch leftExpression;
    private Branch rightExpression;

    public EsBinaryExpression() {
    }

    public Branch getLeftExpression() {
        return leftExpression;
    }

    public Branch getRightExpression() {
        return rightExpression;
    }

    public EsBinaryExpression withLeftExpression(Branch expression) {
        setLeftExpression(expression);
        return this;
    }

    public void setLeftExpression(Branch expression) {
        leftExpression = expression;
    }

    public EsBinaryExpression withRightExpression(Branch expression) {
        setRightExpression(expression);
        return this;
    }

    public void setRightExpression(Branch expression) {
        rightExpression = expression;
    }

    @Override
    public String toString() {
        return // (not ? "NOT " : "") +
                getLeftExpression() + " " + getStringExpression() + " " + getRightExpression();
    }

    public abstract String getStringExpression();

    public <E extends Expression> E getLeftExpression(Class<E> type) {
        return type.cast(getLeftExpression());
    }

    public <E extends Expression> E getRightExpression(Class<E> type) {
        return type.cast(getRightExpression());
    }
}
