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

public class EsGeometryDistance extends EsComparisonOperator {

    public EsGeometryDistance() {
        super("<->");
    }

    public EsGeometryDistance(String operator) {
        super(operator);
    }
}
