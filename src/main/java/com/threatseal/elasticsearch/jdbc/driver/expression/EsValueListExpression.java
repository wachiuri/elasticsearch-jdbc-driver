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

import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsExpressionList;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

/**
 * Models a list of expressions usable as condition.<br>
 * This allows for instance the following expression :
 * <code>"[WHERE] (a, b) [OPERATOR] (c, d)"</code> where "(a, b)" and "(c, d)"
 * are instances of this class.
 */
public class EsValueListExpression extends BranchImpl {

    private EsExpressionList expressionList;

    public EsExpressionList getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(EsExpressionList expressionList) {
        this.expressionList = expressionList;
    }

    @Override
    public String toString() {
        return expressionList.toString();
    }

    public EsValueListExpression withExpressionList(EsExpressionList expressionList) {
        this.setExpressionList(expressionList);
        return this;
    }
}
