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

/**
 * Assignment to a user variable like in select @a = 5.
 */
public class EsVariableAssignment extends BranchImpl{

    private EsUserVariable variable;
    private String operation;
    private Branch expression;

    public EsUserVariable getVariable() {
        return variable;
    }

    public void setVariable(EsUserVariable variable) {
        this.variable = variable;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Branch getExpression() {
        return expression;
    }

    public void setExpression(Branch expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return variable.toString() + " " + operation + " " + expression.toString();
    }

    
}
