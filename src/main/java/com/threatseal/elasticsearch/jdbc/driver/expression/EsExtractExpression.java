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
 * Extract value from date/time expression. The name stores the part - name to get from the
 * following date/time expression.
 *
 * @author tw
 */
public class EsExtractExpression extends BranchImpl{

    private String name;
    private Branch expression;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Branch getExpression() {
        return expression;
    }

    public void setExpression(Branch expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "EXTRACT(" + name + " FROM " + expression + ')';
    }

    public EsExtractExpression withName(String name) {
        this.setName(name);
        return this;
    }

    public EsExtractExpression withExpression(Branch expression) {
        this.setExpression(expression);
        return this;
    }

    public <E extends Branch> E getExpression(Class<E> type) {
        return type.cast(getExpression());
    }
}
