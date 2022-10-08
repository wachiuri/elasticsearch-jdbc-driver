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

public class GreaterThanEquals extends EsComparisonOperator {

    public GreaterThanEquals() {
        super(">=");
    }

    public GreaterThanEquals(String operator) {
        super(operator);
    }
    
    @Override
    public GreaterThanEquals withLeftExpression(Branch arg0) {
        return (GreaterThanEquals) super.withLeftExpression(arg0);
    }

    @Override
    public GreaterThanEquals withRightExpression(Branch arg0) {
        return (GreaterThanEquals) super.withRightExpression(arg0);
    }
}
