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

import java.util.List;
import java.util.Objects;
import net.sf.jsqlparser.expression.JsonAggregateOnNullType;
import net.sf.jsqlparser.expression.JsonAggregateUniqueKeysType;
import net.sf.jsqlparser.expression.JsonFunctionType;
import net.sf.jsqlparser.expression.OrderByClause;
import net.sf.jsqlparser.statement.select.OrderByElement;

/**
 *
 * @author <a href="mailto:andreas@manticore-projects.com">Andreas Reichel</a>
 */

public class EsJsonAggregateFunction extends EsFilterOverImpl {
    private JsonFunctionType functionType;
    
    private Branch expression = null;
    private final OrderByClause expressionOrderBy = new OrderByClause();
    
    private boolean usingKeyKeyword = false;
    private String key;
    private boolean usingValueKeyword = false;
    private Object value;
    
    private boolean usingFormatJson = false;
    
    private JsonAggregateOnNullType onNullType;
    private JsonAggregateUniqueKeysType uniqueKeysType;
    

    public JsonAggregateOnNullType getOnNullType() {
        return onNullType;
    }

    public void setOnNullType(JsonAggregateOnNullType onNullType) {
        this.onNullType = onNullType;
    }
    
    public EsJsonAggregateFunction withOnNullType(JsonAggregateOnNullType onNullType) {
        this.setOnNullType(onNullType);
        return this;
    }

    public JsonAggregateUniqueKeysType getUniqueKeysType() {
        return uniqueKeysType;
    }

    public void setUniqueKeysType(JsonAggregateUniqueKeysType uniqueKeysType) {
        this.uniqueKeysType = uniqueKeysType;
    }
    
    public EsJsonAggregateFunction withUniqueKeysType(JsonAggregateUniqueKeysType uniqueKeysType) {
        this.setUniqueKeysType(uniqueKeysType);
        return this;
    }

    public JsonFunctionType getType() {
        return functionType;
    }
    
    public void setType(JsonFunctionType type) {
        this.functionType = Objects.requireNonNull(type, "The Type of the JSON Aggregate Function must not be null");
    }
    
    public EsJsonAggregateFunction withType(JsonFunctionType type) {
        this.setType(type);
        return this;
    }

    public void setType(String typeName) {
        this.functionType = JsonFunctionType
          .valueOf( Objects.requireNonNull(typeName, "The Type of the JSON Aggregate Function must not be null").toUpperCase());
    }
    
    public EsJsonAggregateFunction withType(String typeName) {
        this.setType(typeName);
        return this;
    }

    public Branch getExpression() {
        return expression;
    }

    public void setExpression(Branch expression) {
        this.expression = expression;
    }
    
    public EsJsonAggregateFunction withExpression(Branch expression) {
        this.setExpression(expression);
        return this;
    }

    public boolean isUsingKeyKeyword() {
        return usingKeyKeyword;
    }

    public void setUsingKeyKeyword(boolean usingKeyKeyword) {
        this.usingKeyKeyword = usingKeyKeyword;
    }
    
    public EsJsonAggregateFunction withUsingKeyKeyword(boolean usingKeyKeyword) {
        this.setUsingKeyKeyword(usingKeyKeyword);
        return this;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    public EsJsonAggregateFunction withKey(String key) {
        this.setKey(key);
        return this;
    }

    public boolean isUsingValueKeyword() {
        return usingValueKeyword;
    }

    public void setUsingValueKeyword(boolean usingValueKeyword) {
        this.usingValueKeyword = usingValueKeyword;
    }
    
    public EsJsonAggregateFunction withUsingValueKeyword(boolean usingValueKeyword) {
        this.setUsingValueKeyword(usingValueKeyword);
        return this;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public EsJsonAggregateFunction withValue(Object value) {
        this.setValue(value);
        return this;
    }
    
    public boolean isUsingFormatJson() {
        return usingFormatJson;
    }

    public void setUsingFormatJson(boolean usingFormatJson) {
        this.usingFormatJson = usingFormatJson;
    }
    
    public EsJsonAggregateFunction withUsingFormatJson(boolean usingFormatJson) {
        this.setUsingFormatJson(usingFormatJson);
        return this;
    }
     
    public List<OrderByElement> getExpressionOrderByElements() {
        return expressionOrderBy.getOrderByElements();
    }

    public void setExpressionOrderByElements(List<OrderByElement> orderByElements) {
        expressionOrderBy.setOrderByElements(orderByElements);
    }
    
    public EsJsonAggregateFunction withExpressionOrderByElements(List<OrderByElement> orderByElements) {
        this.setExpressionOrderByElements(orderByElements);
        return this;
    }
    
    // avoid countless Builder --> String conversion
    @Override
    public StringBuilder append(StringBuilder builder) {
        switch (functionType) {
            case OBJECT:
                appendObject(builder);
                break;
            case ARRAY:
                appendArray(builder);
                break;
            default:
                // this should never happen really
                throw new UnsupportedOperationException("JSON Aggregate Function of the type " + functionType.name() + " has not been implemented yet.");
        }
        return builder;
    }
    
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
    public StringBuilder appendObject(StringBuilder builder) {
        builder.append("JSON_OBJECTAGG( ");
        if (usingValueKeyword) {
            if (usingKeyKeyword) {
                builder.append("KEY ");
            }
            builder.append(key).append(" VALUE ").append(value);
        } else {
            builder.append(key).append(":").append(value);
        }
        
        if (usingFormatJson) {
            builder.append(" FORMAT JSON");
        }
        
        if (onNullType!=null) {
            switch(onNullType) {
                case NULL:
                    builder.append(" NULL ON NULL");
                    break;
                case ABSENT:
                    builder.append(" ABSENT On NULL");
                    break;
                default:
                    // this should never happen
            }
        }
        
        if (uniqueKeysType!=null) {
            switch(uniqueKeysType) {
                case WITH:
                    builder.append(" WITH UNIQUE KEYS");
                    break;
                case WITHOUT:
                    builder.append(" WITHOUT UNIQUE KEYS");
                    break;
                default:
                    // this should never happen
            }
        }
        
        builder.append(" ) ");
        
        
        // FILTER( WHERE expression ) OVER windowNameOrSpecification
        super.append(builder);
        
        return builder;
    }
    
    public void toStringOrderByElements(StringBuilder b) {
        if (expressionOrderBy.getOrderByElements() != null && !expressionOrderBy.getOrderByElements().isEmpty()) {
            b.append("ORDER BY ");
            for (int i = 0; i < expressionOrderBy.getOrderByElements().size(); i++) {
                if (i > 0) {
                    b.append(", ");
                }
                b.append(expressionOrderBy.getOrderByElements().get(i).toString());
            }
        }
    }
    
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
    public StringBuilder appendArray(StringBuilder builder) {
        builder.append("JSON_ARRAYAGG( ");
        builder.append(expression).append(" ");
        
        if (usingFormatJson) {
            builder.append("FORMAT JSON ");
        }
        
        toStringOrderByElements(builder);
        
        if (onNullType!=null) {
            switch(onNullType) {
                case NULL:
                    builder.append(" NULL ON NULL ");
                    break;
                case ABSENT:
                    builder.append(" ABSENT On NULL ");
                    break;
                default:
                    // "ON NULL" was ommitted
            }
        }
        builder.append(") ");
        
        
        // FILTER( WHERE expression ) OVER windowNameOrSpecification
        super.append(builder);
        
        return builder;
    }

    @Override
    public String toString() {
       StringBuilder builder = new StringBuilder();
       return append(builder).toString();
    }
}
