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

public class EsHexValue extends BranchImpl{

    private String value;

    public EsHexValue() {
        // empty constructor
    }

    public EsHexValue(final String value) {
        String val = value;
        this.value = val;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public EsHexValue withValue(String value) {
        this.setValue(value);
        return this;
    }

    @Override
    public String toString() {
        return value;
    }
}
