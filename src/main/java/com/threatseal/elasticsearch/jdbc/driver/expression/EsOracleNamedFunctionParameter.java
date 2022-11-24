/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2021 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression;

import java.util.Objects;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

/**
 *
 * @author <a href="mailto:andreas@manticore-projects.com">Andreas Reichel</a>
 */
public class EsOracleNamedFunctionParameter extends BranchImpl{
    private final String name;
    private final Branch expression;

    public EsOracleNamedFunctionParameter(String name, Branch expression) {
        this.name = Objects.requireNonNull(name, "The NAME of the OracleNamedFunctionParameter must not be null.");
        this.expression = Objects.requireNonNull(expression, "The EXPRESSION of the OracleNamedFunctionParameter must not be null.");
    }

    public String getName() {
        return name;
    }

    public Branch getExpression() {
        return expression;
    }
    
    public StringBuilder appendTo(StringBuilder builder) {
        builder.append(name)
          .append(" => ")
          .append(expression);
        
        return builder;
    }
    
    @Override
    public String toString() {
        return appendTo(new StringBuilder()).toString();
    }
}