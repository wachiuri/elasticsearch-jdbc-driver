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

/**
 * Simple uservariables like @test.
 */
public class EsUserVariable extends BranchImpl {

    private String name;
    private boolean doubleAdd = false;

    public EsUserVariable() {
        // empty constructor
    }

    public EsUserVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDoubleAdd() {
        return doubleAdd;
    }

    public void setDoubleAdd(boolean doubleAdd) {
        this.doubleAdd = doubleAdd;
    }

    @Override
    public String toString() {
        return "@" + (doubleAdd ? "@" : "") + name;
    }

    public EsUserVariable withName(String name) {
        this.setName(name);
        return this;
    }

    public EsUserVariable withDoubleAdd(boolean doubleAdd) {
        this.setDoubleAdd(doubleAdd);
        return this;
    }
}
