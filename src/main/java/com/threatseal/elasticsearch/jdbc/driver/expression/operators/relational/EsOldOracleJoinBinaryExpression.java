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

import net.sf.jsqlparser.expression.BinaryExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import net.sf.jsqlparser.expression.Expression;

public abstract class EsOldOracleJoinBinaryExpression extends EsBinaryExpression implements EsSupportsOldOracleJoinSyntax {

    private int oldOracleJoinSyntax = NO_ORACLE_JOIN;

    private int oraclePriorPosition = NO_ORACLE_PRIOR;

    @Override
    public void setOldOracleJoinSyntax(int oldOracleJoinSyntax) {
        this.oldOracleJoinSyntax = oldOracleJoinSyntax;
        if (oldOracleJoinSyntax < 0 || oldOracleJoinSyntax > 2) {
            throw new IllegalArgumentException("unknown join type for oracle found (type=" + oldOracleJoinSyntax + ")");
        }
    }

    @Override
    public String toString() {
        return //(isNot() ? "NOT " : "")
                (oraclePriorPosition == ORACLE_PRIOR_START ? "PRIOR " : "")
                + getLeftExpression()
                + (oldOracleJoinSyntax == ORACLE_JOIN_RIGHT ? "(+)" : "") + " "
                + getStringExpression() + " "
                + (oraclePriorPosition == ORACLE_PRIOR_END ? "PRIOR " : "")
                + getRightExpression()
                + (oldOracleJoinSyntax == ORACLE_JOIN_LEFT ? "(+)" : "");
    }

    @Override
    public int getOldOracleJoinSyntax() {
        return oldOracleJoinSyntax;
    }

    @Override
    public int getOraclePriorPosition() {
        return oraclePriorPosition;
    }

    @Override
    public void setOraclePriorPosition(int oraclePriorPosition) {
        this.oraclePriorPosition = oraclePriorPosition;
    }

    public EsOldOracleJoinBinaryExpression withOldOracleJoinSyntax(int oldOracleJoinSyntax) {
        this.setOldOracleJoinSyntax(oldOracleJoinSyntax);
        return this;
    }

    public EsOldOracleJoinBinaryExpression withOraclePriorPosition(int oraclePriorPosition) {
        this.setOraclePriorPosition(oraclePriorPosition);
        return this;
    }

    public EsOldOracleJoinBinaryExpression withLeftExpression(Branch arg0) {
        return (EsOldOracleJoinBinaryExpression) super.withLeftExpression(arg0);
    }

    public EsOldOracleJoinBinaryExpression withRightExpression(Branch arg0) {
        return (EsOldOracleJoinBinaryExpression) super.withRightExpression(arg0);
    }
}
