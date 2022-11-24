/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2021 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression;

import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

public final class EsRowGetExpression extends BranchImpl {

    private Branch expression;
    private String columnName;

    public EsRowGetExpression(String columnName) {
    }

    public EsRowGetExpression(Branch expression, String columnName) {
        this.expression = expression;
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        return expression + "." + columnName;
    }

    public Branch getExpression() {
        return expression;
    }

    public void setExpression(Branch expression) {
        this.expression = expression;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
