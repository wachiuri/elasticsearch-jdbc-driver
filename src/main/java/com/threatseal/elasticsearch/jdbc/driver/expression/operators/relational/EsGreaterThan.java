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

public class EsGreaterThan extends EsComparisonOperator {

    public EsGreaterThan() {
        super(">");
    }

    @Override
    public EsGreaterThan withLeftExpression(Branch arg0) {
        return (EsGreaterThan) super.withLeftExpression(arg0);
    }

    @Override
    public EsGreaterThan withRightExpression(Branch arg0) {
        return (EsGreaterThan) super.withRightExpression(arg0);
    }
}
