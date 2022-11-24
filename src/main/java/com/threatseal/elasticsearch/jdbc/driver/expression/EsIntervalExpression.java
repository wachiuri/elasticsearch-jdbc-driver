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

import java.util.Objects;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

public class EsIntervalExpression extends BranchImpl {

    private String parameter = null;
    private String intervalType = null;
    private final boolean intervalKeyword;
    private Branch expression = null;

    public EsIntervalExpression() {
        this(true);
    }

    public EsIntervalExpression(boolean intervalKeyword) {
        this.intervalKeyword = intervalKeyword;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(String intervalType) {
        this.intervalType = intervalType;
    }

    public Branch getExpression() {
        return expression;
    }

    public void setExpression(Branch expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return (intervalKeyword ? "INTERVAL " : "")
                + Objects.toString(expression, parameter)
                + (intervalType != null ? " " + intervalType : "");
    }

    public EsIntervalExpression withParameter(String parameter) {
        this.setParameter(parameter);
        return this;
    }

    public EsIntervalExpression withIntervalType(String intervalType) {
        this.setIntervalType(intervalType);
        return this;
    }

    public EsIntervalExpression withExpression(Branch expression) {
        this.setExpression(expression);
        return this;
    }

    public <E extends Branch> E getExpression(Class<E> type) {
        return type.cast(getExpression());
    }
}
