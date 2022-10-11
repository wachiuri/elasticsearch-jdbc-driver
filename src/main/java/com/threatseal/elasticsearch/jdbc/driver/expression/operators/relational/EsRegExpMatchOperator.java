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

public class EsRegExpMatchOperator extends EsBinaryExpression {

    private RegExpMatchOperatorType operatorType;

    public EsRegExpMatchOperator(RegExpMatchOperatorType operatorType) {
        this.operatorType = Objects.requireNonNull(operatorType, "The provided RegExpMatchOperatorType must not be NULL.");
    }

    public RegExpMatchOperatorType getOperatorType() {
        return operatorType;
    }


    //@Override
    public String getStringExpression() {
        switch (operatorType) {
            case MATCH_CASESENSITIVE:
                return "~";
            case MATCH_CASEINSENSITIVE:
                return "~*";
            case NOT_MATCH_CASESENSITIVE:
                return "!~";
            case NOT_MATCH_CASEINSENSITIVE:
                return "!~*";
            default:
                break;
        }
        return null;
    }

    @Override
    public EsRegExpMatchOperator withLeftExpression(Branch arg0) {
        return (EsRegExpMatchOperator) super.withLeftExpression(arg0);
    }

    @Override
    public EsRegExpMatchOperator withRightExpression(Branch arg0) {
        return (EsRegExpMatchOperator) super.withRightExpression(arg0);
    }
}
