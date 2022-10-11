/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.statement.select;

import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsExpressionList;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

public class EsExpressionListItem {

    private EsExpressionList expressionList;
    private Alias alias;

    public EsExpressionList getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(EsExpressionList expressionList) {
        this.expressionList = expressionList;
    }

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return expressionList + ((alias != null) ? alias.toString() : "");
    }

    public EsExpressionListItem withExpressionList(EsExpressionList expressionList) {
        this.setExpressionList(expressionList);
        return this;
    }

    public EsExpressionListItem withAlias(Alias alias) {
        this.setAlias(alias);
        return this;
    }
}
