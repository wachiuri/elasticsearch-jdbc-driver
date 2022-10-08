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

public class GreaterThan extends EsComparisonOperator {

    public GreaterThan() {
        super(">");
    }

    //@Override
    public void accept(ExpressionVisitor expressionVisitor) {
        //expressionVisitor.visit(this);
    }

    @Override
    public GreaterThan withLeftExpression(Branch arg0) {
        return (GreaterThan) super.withLeftExpression(arg0);
    }

    @Override
    public GreaterThan withRightExpression(Branch arg0) {
        return (GreaterThan) super.withRightExpression(arg0);
    }
}
