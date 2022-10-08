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

public class MinorThanEquals extends EsComparisonOperator {

    public MinorThanEquals() {
        super("<=");
    }

    public MinorThanEquals(String operator) {
        super(operator);
    }

    @Override
    public MinorThanEquals withLeftExpression(Branch arg0) {
        return (MinorThanEquals) super.withLeftExpression(arg0);
    }

    @Override
    public MinorThanEquals withRightExpression(Branch arg0) {
        return (MinorThanEquals) super.withRightExpression(arg0);
    }
}
