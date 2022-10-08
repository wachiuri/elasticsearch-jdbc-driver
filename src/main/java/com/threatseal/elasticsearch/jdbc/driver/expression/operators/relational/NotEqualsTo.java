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

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

public class NotEqualsTo extends EsComparisonOperator {

    public NotEqualsTo() {
        super("<>");
    }

    public NotEqualsTo(String operator) {
        super(operator);
    }

    public NotEqualsTo(Branch left, Branch right) {
        this();
        setLeftExpression(left);
        setRightExpression(right);
    }

    @Override
    public NotEqualsTo withLeftExpression(Branch expression) {
        return (NotEqualsTo) super.withLeftExpression(expression);
    }

    @Override
    public NotEqualsTo withRightExpression(Branch expression) {
        return (NotEqualsTo) super.withRightExpression(expression);
    }
}
