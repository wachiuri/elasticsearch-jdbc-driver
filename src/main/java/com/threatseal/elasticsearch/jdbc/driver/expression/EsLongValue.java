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

import java.math.BigInteger;
import java.util.Objects;

import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

/**
 * Every number without a point or an exponential format is a LongValue.
 */
public class EsLongValue extends BranchImpl {

    private String stringValue;

    public EsLongValue() {
        // empty constructor
    }

    public EsLongValue(final String value) {
        String val = value;
        if (val.charAt(0) == '+') {
            val = val.substring(1);
        }
        this.stringValue = val;
    }

    public EsLongValue(long value) {
        stringValue = String.valueOf(value);
    }

    public long getValue() {
        return Long.parseLong(stringValue);
    }

    public BigInteger getBigIntegerValue() {
        return new BigInteger(stringValue);
    }

    public void setValue(long d) {
        stringValue = String.valueOf(d);
    }

    public EsLongValue withValue(long d) {
        setValue(d);
        return this;
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

    public EsLongValue withStringValue(String stringValue) {
        this.setStringValue(stringValue);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EsLongValue longValue = (EsLongValue) o;
        return stringValue.equals(longValue.stringValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stringValue);
    }
}
