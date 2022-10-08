/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2022 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational;

import net.sf.jsqlparser.expression.ExpressionVisitor;

public class GeometryDistance extends EsComparisonOperator {

    public GeometryDistance() {
        super("<->");
    }

    public GeometryDistance(String operator) {
        super(operator);
    }

    //@Override
    public void accept(ExpressionVisitor expressionVisitor) {
        //expressionVisitor.visit(this);
    }
}
