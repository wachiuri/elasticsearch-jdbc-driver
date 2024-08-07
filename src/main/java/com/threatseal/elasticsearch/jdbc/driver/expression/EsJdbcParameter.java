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
 * A '?' in a statement or a ?&lt;number&gt; e.g. ?4
 */
public class EsJdbcParameter extends BranchImpl{

    private Integer index;
    private boolean useFixedIndex = false;

    public EsJdbcParameter() {
    }

    public EsJdbcParameter(Integer index, boolean useFixedIndex) {
        this.index = index;
        this.useFixedIndex = useFixedIndex;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public boolean isUseFixedIndex() {
        return useFixedIndex;
    }

    public void setUseFixedIndex(boolean useFixedIndex) {
        this.useFixedIndex = useFixedIndex;
    }

    @Override
    public String toString() {
        return useFixedIndex ? "?" + index : "?";
    }

    public EsJdbcParameter withIndex(Integer index) {
        this.setIndex(index);
        return this;
    }

    public EsJdbcParameter withUseFixedIndex(boolean useFixedIndex) {
        this.setUseFixedIndex(useFixedIndex);
        return this;
    }
}
