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

import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

public class EsIsDistinctExpression extends EsBinaryExpression {

    private boolean not = false;

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean b) {
        not = b;
    }

    @Override
    public String getStringExpression() {
        return " IS " + (isNot() ? "NOT " : "") + "DISTINCT FROM ";
    }

    @Override
    public String toString() {
        String retval = getLeftExpression() + getStringExpression() + getRightExpression();
        return retval;
    }
}
