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
import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

public class EsJsonOperator extends EsBinaryExpression {

    private String op; //"@>"

    public EsJsonOperator(String op) {
        this.op = op;
    }

    @Override
    public String getStringExpression() {
        return op;
    }

    @Override
    public EsJsonOperator withLeftExpression(Branch arg0) {
        return (EsJsonOperator) super.withLeftExpression(arg0);
    }

    @Override
    public EsJsonOperator withRightExpression(Branch arg0) {
        return (EsJsonOperator) super.withRightExpression(arg0);
    }
}
