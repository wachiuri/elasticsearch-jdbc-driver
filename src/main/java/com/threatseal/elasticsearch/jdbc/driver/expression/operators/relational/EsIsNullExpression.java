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
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;

public class EsIsNullExpression extends BranchImpl {

    private Branch leftExpression;
    private boolean not = false;
    private boolean useIsNull = false;

    public Branch getLeftExpression() {
        return leftExpression;
    }

    public boolean isNot() {
        return not;
    }

    public void setLeftExpression(Branch expression) {
        leftExpression = expression;
    }

    public void setNot(boolean b) {
        not = b;
    }

    public boolean isUseIsNull() {
        return useIsNull;
    }

    public void setUseIsNull(boolean useIsNull) {
        this.useIsNull = useIsNull;
    }

    @Override
    public String toString() {
        if (isUseIsNull()) {
            return leftExpression + (not ? " NOT" : "") + " ISNULL";
        } else {
            return leftExpression + " IS " + (not ? "NOT " : "") + "NULL";
        }
    }

    public EsIsNullExpression withUseIsNull(boolean useIsNull) {
        this.setUseIsNull(useIsNull);
        return this;
    }

    public EsIsNullExpression withLeftExpression(Branch leftExpression) {
        this.setLeftExpression(leftExpression);
        return this;
    }

    public EsIsNullExpression withNot(boolean not) {
        this.setNot(not);
        return this;
    }

    public <E extends Branch> E getLeftExpression(Class<E> type) {
        return type.cast(getLeftExpression());
    }
}
