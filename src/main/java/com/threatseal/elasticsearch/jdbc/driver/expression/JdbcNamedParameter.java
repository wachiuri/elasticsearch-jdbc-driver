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

public class JdbcNamedParameter extends com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl{

    private String name;

    public JdbcNamedParameter() {
    }

    public JdbcNamedParameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ":" + name;
    }

    public JdbcNamedParameter withName(String name) {
        this.setName(name);
        return this;
    }
}
