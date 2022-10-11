/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression.operators.conditional;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

public class EsAndExpression extends EsConditionalOperator {
    private boolean useOperator = false;

    public EsAndExpression() {
        // nothing
    }

    public EsAndExpression(Branch leftExpression, Branch rightExpression) {
        setLeftExpression(leftExpression);
        setRightExpression(rightExpression);
    }

    public void setUseOperator(boolean useOperator) {
        this.useOperator = useOperator;
    }

    public boolean isUseOperator() {
        return useOperator;
    }

    @Override
    public String getStringExpression() {
        return useOperator ? "&&" : "AND";
    }

    public EsAndExpression withUseOperator(boolean useOperator) {
        this.setUseOperator(useOperator);
        return this;
    }

    @Override
    public EsAndExpression withLeftExpression(Branch arg0) {
        return (EsAndExpression) super.withLeftExpression(arg0);
    }

    @Override
    public EsAndExpression withRightExpression(Branch arg0) {
        return (EsAndExpression) super.withRightExpression(arg0);
    }
}
