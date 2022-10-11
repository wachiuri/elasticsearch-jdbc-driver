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

public class EsLikeExpression extends EsBinaryExpression {

    private boolean not = false;
    private Branch escapeExpression = null;
    private boolean caseInsensitive = false;

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean b) {
        not = b;
    }

    @Override
    public String getStringExpression() {
        return caseInsensitive ? "ILIKE" : "LIKE";
    }

    @Override
    public String toString() {
        String retval = getLeftExpression() + " " + (not ? "NOT " : "") + getStringExpression() + " " + getRightExpression();
        if (escapeExpression != null) {
            retval += " ESCAPE "  + escapeExpression ;
        }

        return retval;
    }

    public Branch getEscape() {
        return escapeExpression;
    }

    public void setEscape(Branch escapeExpression) {
        this.escapeExpression = escapeExpression;
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    public EsLikeExpression withEscape(Branch escape) {
        this.setEscape(escape);
        return this;
    }

    public EsLikeExpression withCaseInsensitive(boolean caseInsensitive) {
        this.setCaseInsensitive(caseInsensitive);
        return this;
    }

    public EsLikeExpression withNot(boolean not) {
        this.setNot(not);
        return this;
    }

    @Override
    public EsLikeExpression withLeftExpression(Branch arg0) {
        return (EsLikeExpression) super.withLeftExpression(arg0);
    }

    @Override
    public EsLikeExpression withRightExpression(Branch arg0) {
        return (EsLikeExpression) super.withRightExpression(arg0);
    }
}
