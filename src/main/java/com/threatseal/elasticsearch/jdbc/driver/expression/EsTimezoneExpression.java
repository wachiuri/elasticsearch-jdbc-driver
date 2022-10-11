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

public class EsTimezoneExpression extends BranchImpl{

    private Branch leftExpression;
    private ArrayList<Branch> timezoneExpressions = new ArrayList<>();

    public Branch getLeftExpression() {
        return leftExpression;
    }

    public void setLeftExpression(Branch expression) {
        leftExpression = expression;
    }

    public List<Branch> getTimezoneExpressions() {
        return timezoneExpressions;
    }

    public void addTimezoneExpression(Branch timezoneExpr) {
        this.timezoneExpressions.add(timezoneExpr);
    }

    @Override
    public String toString() {
        String returnValue = getLeftExpression().toString();
        for (Branch expr : timezoneExpressions) {
            returnValue += " AT TIME ZONE " + expr.toString();
        }

        return returnValue;
    }
}
