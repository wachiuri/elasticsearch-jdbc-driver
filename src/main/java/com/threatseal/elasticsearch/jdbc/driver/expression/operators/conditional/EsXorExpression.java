/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2021 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression.operators.conditional;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

public class EsXorExpression extends EsConditionalOperator {

    public EsXorExpression() {
        // nothing
    }

    public EsXorExpression(Branch leftExpression, Branch rightExpression) {
        setLeftExpression(leftExpression);
        setRightExpression(rightExpression);
    }

    @Override
    public EsXorExpression withLeftExpression(Branch expression) {
        return (EsXorExpression) super.withLeftExpression(expression);
    }

    @Override
    public EsXorExpression withRightExpression(Branch expression) {
        return (EsXorExpression) super.withRightExpression(expression);
    }

    @Override
    public String getStringExpression() {
        return "XOR";
    }

}
