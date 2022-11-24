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
import java.util.Objects;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperatorType;

public class EsRegExpMySQLOperator extends EsBinaryExpression {

    private RegExpMatchOperatorType operatorType;
    private boolean useRLike = false;
    private boolean not = false;

    public EsRegExpMySQLOperator(RegExpMatchOperatorType operatorType) {
        this(false, operatorType);
    }

    public EsRegExpMySQLOperator(boolean not, RegExpMatchOperatorType operatorType) {
        this.operatorType = Objects.requireNonNull(operatorType, "The provided RegExpMatchOperatorType must not be NULL.");
        this.not = not;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    public RegExpMatchOperatorType getOperatorType() {
        return operatorType;
    }

    public boolean isUseRLike() {
        return useRLike;
    }

    public EsRegExpMySQLOperator useRLike() {
        useRLike = true;
        return this;
    }

    @Override
    public String getStringExpression() {
        return (not ? "NOT " : "")
                + (useRLike ? "RLIKE" : "REGEXP")
                + (operatorType == RegExpMatchOperatorType.MATCH_CASESENSITIVE ? " BINARY" : "");
    }

    @Override
    public EsRegExpMySQLOperator withLeftExpression(Branch arg0) {
        return (EsRegExpMySQLOperator) super.withLeftExpression(arg0);
    }

    @Override
    public EsRegExpMySQLOperator withRightExpression(Branch arg0) {
        return (EsRegExpMySQLOperator) super.withRightExpression(arg0);
    }
}
