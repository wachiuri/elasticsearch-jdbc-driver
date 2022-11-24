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

import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

/**
 * It represents a "-" or "+" or "~" before an expression
 */
public class EsSignedExpression extends BranchImpl {

    private char sign;

    public EsSignedExpression() {
        // empty constructor
    }

    public EsSignedExpression(char sign, Branch child) {
        setSign(sign);
        setChild(child);
    }

    public char getSign() {
        return sign;
    }

    public final void setSign(char sign) {
        this.sign = sign;
        if (sign != '+' && sign != '-' && sign != '~') {
            throw new IllegalArgumentException("illegal sign character, only + - ~ allowed");
        }
    }

    @Override
    public String toString() {
        return getSign() + getChild().toString();
    }
    
    public Object toObject(){
        return toString();
    }

}
