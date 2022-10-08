/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression;

import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

import java.util.ArrayList;
import java.util.List;

public class TimezoneExpression extends com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl{

    private Expression leftExpression;
    private ArrayList<Expression> timezoneExpressions = new ArrayList<>();

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public void setLeftExpression(Expression expression) {
        leftExpression = expression;
    }

    public List<Expression> getTimezoneExpressions() {
        return timezoneExpressions;
    }

    public void addTimezoneExpression(Expression timezoneExpr) {
        this.timezoneExpressions.add(timezoneExpr);
    }

    @Override
    public String toString() {
        String returnValue = getLeftExpression().toString();
        for (Expression expr : timezoneExpressions) {
            returnValue += " AT TIME ZONE " + expr.toString();
        }

        return returnValue;
    }
}
