/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.schema;

import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;

/**
 *
 * @author Timothy Wachiuri
 */
public class EsColumn extends BranchImpl {

    private String columnName;

    public EsColumn() {
    }

    public EsColumn(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        return columnName;
    }

    @Override
    public Object toObject() {
        return toString();
    }

}
