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

public class EsCollateExpression extends BranchImpl{

    private Branch leftExpression;
    private String collate;

    public EsCollateExpression() {
        // empty constructor
    }

    public EsCollateExpression(Branch leftExpression, String collate) {
        this.leftExpression = leftExpression;
        this.collate = collate;
    }

    public Branch getLeftExpression() {
        return leftExpression;
    }

    public void setLeftExpression(Branch leftExpression) {
        this.leftExpression = leftExpression;
    }

    public String getCollate() {
        return collate;
    }

    public void setCollate(String collate) {
        this.collate = collate;
    }

    @Override
    public String toString() {
        return leftExpression.toString() + " COLLATE " + collate;
    }

    public EsCollateExpression withLeftExpression(Branch leftExpression) {
        this.setLeftExpression(leftExpression);
        return this;
    }

    public EsCollateExpression withCollate(String collate) {
        this.setCollate(collate);
        return this;
    }

    public <E extends Branch> E getLeftExpression(Class<E> type) {
        return type.cast(getLeftExpression());
    }
}
