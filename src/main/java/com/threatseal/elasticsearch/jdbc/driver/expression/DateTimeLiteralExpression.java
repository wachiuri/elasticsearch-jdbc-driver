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

public class DateTimeLiteralExpression extends com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl{

    private String value;
    private DateTime type;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DateTime getType() {
        return type;
    }

    public void setType(DateTime type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.name() + " " + value;
    }

    public DateTimeLiteralExpression withValue(String value) {
        this.setValue(value);
        return this;
    }

    public DateTimeLiteralExpression withType(DateTime type) {
        this.setType(type);
        return this;
    }

    public enum DateTime {
        DATE, TIME, TIMESTAMP, TIMESTAMPTZ;
    }
}
