/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
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

public class EsOrExpression extends EsConditionalOperator {

    public EsOrExpression() {
        // nothing
    }

    public EsOrExpression(Branch leftExpression, Branch rightExpression) {
        setLeftExpression(leftExpression);
        setRightExpression(rightExpression);
    }

    @Override
    public EsOrExpression withLeftExpression(Branch expression) {
        return (EsOrExpression) super.withLeftExpression(expression);
    }

    @Override
    public EsOrExpression withRightExpression(Branch expression) {
        return (EsOrExpression) super.withRightExpression(expression);
    }

    @Override
    public String getStringExpression() {
        return "OR";
    }

}
