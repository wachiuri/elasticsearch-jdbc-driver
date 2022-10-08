/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational;

import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.Expression;

public abstract class EsComparisonOperator extends EsBinaryExpression {

    private final String operator;

    public EsComparisonOperator(String operator) {
        this.operator = operator;
    }

    public EsComparisonOperator(String operator, Branch left, Branch right) {
        this(operator);
        setLeftExpression(left);
        setRightExpression(right);
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String getStringExpression() {
        return operator;
    }

    @Override
    public EsComparisonOperator withLeftExpression(Branch arg0) {
        return (EsComparisonOperator) super.withLeftExpression(arg0);
    }

    @Override
    public EsComparisonOperator withRightExpression(Branch arg0) {
        return (EsComparisonOperator) super.withRightExpression(arg0);
    }
}
