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
 * A clause of following syntax: WHEN condition THEN expression. Which is part
 * of a CaseExpression.
 */
public class EsWhenClause extends BranchImpl {

    private Branch whenExpression;
    private Branch thenExpression;

    public Branch getThenExpression() {
        return thenExpression;
    }

    public void setThenExpression(Branch thenExpression) {
        this.thenExpression = thenExpression;
    }

    /**
     * @return Returns the whenExpression.
     */
    public Branch getWhenExpression() {
        return whenExpression;
    }

    /**
     * @param whenExpression The whenExpression to set.
     */
    public void setWhenExpression(Branch whenExpression) {
        this.whenExpression = whenExpression;
    }

    @Override
    public String toString() {
        return "WHEN " + whenExpression + " THEN " + thenExpression;
    }

    public EsWhenClause withWhenExpression(Branch whenExpression) {
        this.setWhenExpression(whenExpression);
        return this;
    }

    public EsWhenClause withThenExpression(Branch thenExpression) {
        this.setThenExpression(thenExpression);
        return this;
    }

    public <E extends Branch> E getThenExpression(Class<E> type) {
        return type.cast(getThenExpression());
    }

    public <E extends Branch> E getWhenExpression(Class<E> type) {
        return type.cast(getWhenExpression());
    }
}
