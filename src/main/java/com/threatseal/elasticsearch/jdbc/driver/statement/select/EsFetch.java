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

public class EsFetch {

    private long rowCount;
    private EsJdbcParameter fetchJdbcParameter = null;
    private boolean isFetchParamFirst = false;
    private String fetchParam = "ROW";

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(long l) {
        rowCount = l;
    }

    public EsJdbcParameter getFetchJdbcParameter() {
        return fetchJdbcParameter;
    }

    public String getFetchParam() {
        return fetchParam;
    }

    public boolean isFetchParamFirst() {
        return isFetchParamFirst;
    }

    public void setFetchJdbcParameter(EsJdbcParameter jdbc) {
        fetchJdbcParameter = jdbc;
    }

    public void setFetchParam(String s) {
        this.fetchParam = s;
    }

    public void setFetchParamFirst(boolean b) {
        this.isFetchParamFirst = b;
    }

    @Override
    public String toString() {
        return " FETCH " + (isFetchParamFirst ? "FIRST" : "NEXT") + " " 
                + (fetchJdbcParameter!=null ? fetchJdbcParameter.toString() : 
                    Long.toString(rowCount)) + " " + fetchParam + " ONLY";
    }

    public EsFetch withRowCount(long rowCount) {
        this.setRowCount(rowCount);
        return this;
    }

    public EsFetch withFetchJdbcParameter(EsJdbcParameter fetchJdbcParameter) {
        this.setFetchJdbcParameter(fetchJdbcParameter);
        return this;
    }

    public EsFetch withFetchParam(String fetchParam) {
        this.setFetchParam(fetchParam);
        return this;
    }
}
