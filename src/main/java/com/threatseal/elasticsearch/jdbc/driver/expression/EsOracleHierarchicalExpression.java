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

public class EsOracleHierarchicalExpression extends BranchImpl{

    private Branch startExpression;
    private Branch connectExpression;
    private boolean noCycle = false;
    boolean connectFirst = false;

    public Branch getStartExpression() {
        return startExpression;
    }

    public void setStartExpression(Branch startExpression) {
        this.startExpression = startExpression;
    }

    public Branch getConnectExpression() {
        return connectExpression;
    }

    public void setConnectExpression(Branch connectExpression) {
        this.connectExpression = connectExpression;
    }

    public boolean isNoCycle() {
        return noCycle;
    }

    public void setNoCycle(boolean noCycle) {
        this.noCycle = noCycle;
    }

    public boolean isConnectFirst() {
        return connectFirst;
    }

    public void setConnectFirst(boolean connectFirst) {
        this.connectFirst = connectFirst;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (isConnectFirst()) {
            b.append(" CONNECT BY ");
            if (isNoCycle()) {
                b.append("NOCYCLE ");
            }
            b.append(connectExpression.toString());
            if (startExpression != null) {
                b.append(" START WITH ").append(startExpression.toString());
            }
        } else {
            if (startExpression != null) {
                b.append(" START WITH ").append(startExpression.toString());
            }
            b.append(" CONNECT BY ");
            if (isNoCycle()) {
                b.append("NOCYCLE ");
            }
            b.append(connectExpression.toString());
        }
        return b.toString();
    }

    public EsOracleHierarchicalExpression withStartExpression(Branch startExpression) {
        this.setStartExpression(startExpression);
        return this;
    }

    public EsOracleHierarchicalExpression withConnectExpression(Branch connectExpression) {
        this.setConnectExpression(connectExpression);
        return this;
    }

    public EsOracleHierarchicalExpression withNoCycle(boolean noCycle) {
        this.setNoCycle(noCycle);
        return this;
    }

    public EsOracleHierarchicalExpression withConnectFirst(boolean connectFirst) {
        this.setConnectFirst(connectFirst);
        return this;
    }

    public <E extends Branch> E getStartExpression(Class<E> type) {
        return type.cast(getStartExpression());
    }

    public <E extends Branch> E getConnectExpression(Class<E> type) {
        return type.cast(getConnectExpression());
    }
}
