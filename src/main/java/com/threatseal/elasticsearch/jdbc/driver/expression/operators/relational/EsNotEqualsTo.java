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

public class EsNotEqualsTo extends EsComparisonOperator {

    public EsNotEqualsTo() {
        super("<>");
    }

    public EsNotEqualsTo(String operator) {
        super(operator);
    }

    public EsNotEqualsTo(Branch left, Branch right) {
        this();
        setLeftExpression(left);
        setRightExpression(right);
    }

    @Override
    public EsNotEqualsTo withLeftExpression(Branch expression) {
        return (EsNotEqualsTo) super.withLeftExpression(expression);
    }

    @Override
    public EsNotEqualsTo withRightExpression(Branch expression) {
        return (EsNotEqualsTo) super.withRightExpression(expression);
    }
}
