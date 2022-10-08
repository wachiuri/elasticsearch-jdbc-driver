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

public class TimeKeyExpression extends com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl{

    private String stringValue;

    public TimeKeyExpression() {
        // empty constructor
    }

    public TimeKeyExpression(final String value) {
        this.stringValue = value;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String string) {
        stringValue = string;
    }

    @Override
    public String toString() {
        return getStringValue();
    }

    public TimeKeyExpression withStringValue(String stringValue) {
        this.setStringValue(stringValue);
        return this;
    }
}
