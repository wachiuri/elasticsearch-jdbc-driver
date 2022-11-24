/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.statement.select;

import com.threatseal.elasticsearch.jdbc.driver.expression.EsJdbcParameter;

public class EsFirst {

    public enum Keyword {
        FIRST,
        LIMIT
    }

    private Keyword keyword;
    private Long rowCount;
    private EsJdbcParameter jdbcParameter;
    private String variable;

    public Long getRowCount() {
        return rowCount;
    }

    public void setRowCount(Long rowCount) {
        this.rowCount = rowCount;
    }

    public EsJdbcParameter getJdbcParameter() {
        return jdbcParameter;
    }

    public void setJdbcParameter(EsJdbcParameter jdbcParameter) {
        this.jdbcParameter = jdbcParameter;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        String result = keyword.name() + " ";

        if (rowCount != null) {
            result += rowCount;
        } else if (jdbcParameter != null) {
            result += jdbcParameter.toString();
        } else if (variable != null) {
            result += variable;
        }

        return result;
    }
    
    public EsFirst withKeyword(Keyword keyword) {
        this.setKeyword(keyword);
        return this;
    }

    public EsFirst withRowCount(Long rowCount) {
        this.setRowCount(rowCount);
        return this;
    }

    public EsFirst withJdbcParameter(EsJdbcParameter jdbcParameter) {
        this.setJdbcParameter(jdbcParameter);
        return this;
    }

    public EsFirst withVariable(String variable) {
        this.setVariable(variable);
        return this;
    }
}
