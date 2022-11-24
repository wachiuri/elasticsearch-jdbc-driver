/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2022 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression;

import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsExpressionList;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

public class EsOverlapsCondition extends BranchImpl{


    private EsExpressionList left;
    private EsExpressionList right;


    public EsOverlapsCondition(EsExpressionList left, EsExpressionList right) {
        this.left = left;
        this.right = right;
    }

    public EsExpressionList getLeft() {
        return left;
    }

    public EsExpressionList getRight() {
        return right;
    }

    @Override
    public String toString() {
        return String.format("%s OVERLAPS %s"
                , left.toString()
                , right.toString()
        );
    }
}
