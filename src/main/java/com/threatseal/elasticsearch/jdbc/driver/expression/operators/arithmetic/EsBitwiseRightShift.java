/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

public class EsBitwiseRightShift extends EsBinaryExpression {

    @Override
    public String getStringExpression() {
        return ">>";
    }

    @Override
    public EsBitwiseRightShift withLeftExpression(Branch arg0) {
        return (EsBitwiseRightShift) super.withLeftExpression(arg0);
    }

    @Override
    public EsBitwiseRightShift withRightExpression(Branch arg0) {
        return (EsBitwiseRightShift) super.withRightExpression(arg0);
    }
}