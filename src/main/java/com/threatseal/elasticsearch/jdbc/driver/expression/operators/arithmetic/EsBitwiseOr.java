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

public class EsBitwiseOr extends EsBinaryExpression {

    @Override
    public String getStringExpression() {
        return "|";
    }

    @Override
    public EsBitwiseOr withLeftExpression(Branch arg0) {
        return (EsBitwiseOr) super.withLeftExpression(arg0);
    }

    @Override
    public EsBitwiseOr withRightExpression(Branch arg0) {
        return (EsBitwiseOr) super.withRightExpression(arg0);
    }
}
