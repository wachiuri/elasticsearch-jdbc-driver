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

import java.sql.Time;

/**
 * A Time in the form {t 'hh:mm:ss'}
 */
public class EsTimeValue extends BranchImpl{

    private Time value;

    public EsTimeValue() {
        // empty constructor
    }

    public EsTimeValue(String value) {
        this.value = Time.valueOf(value.substring(1, value.length() - 1));
    }

    public Time getValue() {
        return value;
    }

    public void setValue(Time d) {
        value = d;
    }

    @Override
    public String toString() {
        return "{t '" + value + "'}";
    }

    public EsTimeValue withValue(Time value) {
        this.setValue(value);
        return this;
    }
}
