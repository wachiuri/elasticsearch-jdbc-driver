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
import net.sf.jsqlparser.statement.create.table.ColDataType;

public class EsTryCastExpression extends BranchImpl{

    private Branch leftExpression;
    private ColDataType type;
    private EsRowConstructor rowConstructor;
    private boolean useCastKeyword = true;

    public EsRowConstructor getRowConstructor() {
        return rowConstructor;
    }

    public void setRowConstructor(EsRowConstructor rowConstructor) {
        this.rowConstructor = rowConstructor;
        this.type = null;
    }

    public EsTryCastExpression withRowConstructor(EsRowConstructor rowConstructor) {
        setRowConstructor(rowConstructor);
        return this;
    }

    public ColDataType getType() {
        return type;
    }

    public void setType(ColDataType type) {
        this.type = type;
        this.rowConstructor = null;
    }

    public Branch getLeftExpression() {
        return leftExpression;
    }

    public void setLeftExpression(Branch expression) {
        leftExpression = expression;
    }

    public boolean isUseCastKeyword() {
        return useCastKeyword;
    }

    public void setUseCastKeyword(boolean useCastKeyword) {
        this.useCastKeyword = useCastKeyword;
    }

    @Override
    public String toString() {
        if (useCastKeyword) {
            return rowConstructor!=null
              ? "TRY_CAST(" + leftExpression + " AS " + rowConstructor.toString() + ")"
              : "TRY_CAST(" + leftExpression + " AS " + type.toString() + ")";
        } else {
            return leftExpression + "::" + type.toString();
        }
    }

    public EsTryCastExpression withType(ColDataType type) {
        this.setType(type);
        return this;
    }

    public EsTryCastExpression withUseCastKeyword(boolean useCastKeyword) {
        this.setUseCastKeyword(useCastKeyword);
        return this;
    }

    public EsTryCastExpression withLeftExpression(Branch leftExpression) {
        this.setLeftExpression(leftExpression);
        return this;
    }

    public <E extends Branch> E getLeftExpression(Class<E> type) {
        return type.cast(getLeftExpression());
    }
}
