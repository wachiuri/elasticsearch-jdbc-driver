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

import java.sql.Date;

/**
 * A Date in the form {d 'yyyy-mm-dd'}
 */
public class EsDateValue extends com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl{

    private Date value;

    public EsDateValue() {
        // empty constructor
    }

    public EsDateValue(Date value) {
        this.value = value;
    }

    /**
     * A Date in the form {d 'yyyy-mm-dd'}
     *
     * @param value The text presentation of the Date to be parsed.
     */
    public EsDateValue(String value) {
        this(Date.valueOf(value.substring(1, value.length() - 1)));
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date d) {
        value = d;
    }

    @Override
    public String toString() {
        return "{d '" + value.toString() + "'}";
    }

    public EsDateValue withValue(Date value) {
        this.setValue(value);
        return this;
    }
}
