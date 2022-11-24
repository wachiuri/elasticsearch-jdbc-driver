/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2021 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression;

import java.util.ArrayList;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.util.List;

public class EsArrayConstructor extends BranchImpl {

    private List<Branch> expressions=new ArrayList<>();
    private boolean arrayKeyword;

    public List<Branch> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Branch> expressions) {
        this.expressions = expressions;
    }

    public boolean isArrayKeyword() {
        return arrayKeyword;
    }

    public void setArrayKeyword(boolean arrayKeyword) {
        this.arrayKeyword = arrayKeyword;
    }

    public EsArrayConstructor(boolean arrayKeyword) {
    }

    public EsArrayConstructor(List<Branch> expressions, boolean arrayKeyword) {
        this.expressions = expressions;
        this.arrayKeyword = arrayKeyword;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (arrayKeyword) {
            sb.append("ARRAY");
        }
        sb.append("[");
        sb.append(PlainSelect.getStringList(expressions, true, false));
        sb.append("]");
        return sb.toString();
    }
}
