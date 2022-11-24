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
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;

public class EsSimilarToExpression extends EsBinaryExpression {

    private boolean not = false;
    private String escape = null;

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean b) {
        not = b;
    }

    @Override
    public String getStringExpression() {
        return "SIMILAR TO";
    }

    @Override
    public String toString() {
        String retval = getLeftExpression() + " " + (not ? "NOT " : "") + getStringExpression() + " " + getRightExpression();
        if (escape != null) {
            retval += " ESCAPE " + "'" + escape + "'";
        }

        return retval;
    }

    public String getEscape() {
        return escape;
    }

    public void setEscape(String escape) {
        this.escape = escape;
    }

    public EsSimilarToExpression withEscape(String escape) {
        this.setEscape(escape);
        return this;
    }

    public EsSimilarToExpression withNot(boolean not) {
        this.setNot(not);
        return this;
    }

    @Override
    public EsSimilarToExpression withLeftExpression(Branch arg0) {
        return (EsSimilarToExpression) super.withLeftExpression(arg0);
    }

    @Override
    public EsSimilarToExpression withRightExpression(Branch arg0) {
        return (EsSimilarToExpression) super.withRightExpression(arg0);
    }
}
