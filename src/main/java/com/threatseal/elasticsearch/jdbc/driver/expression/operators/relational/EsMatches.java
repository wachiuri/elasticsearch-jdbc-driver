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

public class EsMatches extends EsOldOracleJoinBinaryExpression {

    @Override
    public String getStringExpression() {
        return "@@";
    }

    @Override
    public EsMatches withLeftExpression(Branch arg0) {
        return (EsMatches) super.withLeftExpression(arg0);
    }

    @Override
    public EsMatches withRightExpression(Branch arg0) {
        return (EsMatches) super.withRightExpression(arg0);
    }

    @Override
    public EsMatches withOldOracleJoinSyntax(int oldOracleJoinSyntax) {
        return (EsMatches) super.withOldOracleJoinSyntax(oldOracleJoinSyntax);
    }

    @Override
    public EsMatches withOraclePriorPosition(int oraclePriorPosition) {
        return (EsMatches) super.withOraclePriorPosition(oraclePriorPosition);
    }
}
