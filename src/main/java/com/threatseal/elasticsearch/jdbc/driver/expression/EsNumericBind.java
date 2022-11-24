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

public class EsNumericBind extends BranchImpl{

    private int bindId;

    public int getBindId() {
        return bindId;
    }

    public void setBindId(int bindId) {
        this.bindId = bindId;
    }

    @Override
    public String toString() {
        return ":" + bindId;
    }

    public EsNumericBind withBindId(int bindId) {
        this.setBindId(bindId);
        return this;
    }
}
