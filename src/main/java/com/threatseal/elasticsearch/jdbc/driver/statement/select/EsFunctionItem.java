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

import com.threatseal.elasticsearch.jdbc.driver.expression.EsFunction;
import net.sf.jsqlparser.Model;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;

public class EsFunctionItem implements Model {

    private EsFunction function;
    private Alias alias;

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public EsFunction getFunction() {
        return function;
    }

    public void setFunction(EsFunction function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return function + ((alias != null) ? alias.toString() : "");
    }

    public EsFunctionItem withFunction(EsFunction function) {
        this.setFunction(function);
        return this;
    }

    public EsFunctionItem withAlias(Alias alias) {
        this.setAlias(alias);
        return this;
    }
}
