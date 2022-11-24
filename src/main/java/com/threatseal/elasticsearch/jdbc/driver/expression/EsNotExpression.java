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
 * It represents a "not " or "!" before an expression.
 */
public class EsNotExpression extends BranchImpl{

    private Branch expression;

    private boolean exclamationMark = false;

    public EsNotExpression() {
        // empty constructor
    }

    public EsNotExpression(Branch expression) {
        this(expression, false);
    }

    public EsNotExpression(Branch expression, boolean useExclamationMark) {
        setExpression(expression);
        this.exclamationMark = useExclamationMark;
    }

    public Branch getExpression() {
        return expression;
    }

    public final void setExpression(Branch expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return (exclamationMark ? "! " : "NOT ") + expression.toString();
    }

    public boolean isExclamationMark() {
        return exclamationMark;
    }

    public void setExclamationMark(boolean exclamationMark) {
        this.exclamationMark = exclamationMark;
    }

    public EsNotExpression withExpression(Branch expression) {
        this.setExpression(expression);
        return this;
    }

    public EsNotExpression withExclamationMark(boolean exclamationMark) {
        this.setExclamationMark(exclamationMark);
        return this;
    }

    public <E extends Branch> E getExpression(Class<E> type) {
        return type.cast(getExpression());
    }
}
