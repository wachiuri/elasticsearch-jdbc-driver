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

public class Matches extends OldOracleJoinBinaryExpression {

    @Override
    public String getStringExpression() {
        return "@@";
    }

    @Override
    public Matches withLeftExpression(Branch arg0) {
        return (Matches) super.withLeftExpression(arg0);
    }

    @Override
    public Matches withRightExpression(Branch arg0) {
        return (Matches) super.withRightExpression(arg0);
    }

    @Override
    public Matches withOldOracleJoinSyntax(int oldOracleJoinSyntax) {
        return (Matches) super.withOldOracleJoinSyntax(oldOracleJoinSyntax);
    }

    @Override
    public Matches withOraclePriorPosition(int oraclePriorPosition) {
        return (Matches) super.withOraclePriorPosition(oraclePriorPosition);
    }
}
