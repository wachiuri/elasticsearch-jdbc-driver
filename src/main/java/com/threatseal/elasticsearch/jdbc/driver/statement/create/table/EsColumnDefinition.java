/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.statement.create.table;

import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * Globally used definition class for columns.
 */
public class EsColumnDefinition extends BranchImpl {

    private String columnName;
    private ColDataType colDataType;
    private List<String> columnSpecs;

    public EsColumnDefinition() {
    }

    public EsColumnDefinition(String columnName, ColDataType colDataType) {
        this.columnName = columnName;
        this.colDataType = colDataType;
    }

    public EsColumnDefinition(String columnName, ColDataType colDataType, List<String> columnSpecs) {
        this(columnName, colDataType);
        this.columnSpecs = columnSpecs;
    }

    public List<String> getColumnSpecs() {
        return columnSpecs;
    }

    public void setColumnSpecs(List<String> list) {
        columnSpecs = list;
    }

    public ColDataType getColDataType() {
        return colDataType;
    }

    public void setColDataType(ColDataType type) {
        colDataType = type;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String string) {
        columnName = string;
    }

    @Override
    public String toString() {
        return columnName + " " + toStringDataTypeAndSpec();
    }

    public String toStringDataTypeAndSpec() {
        return colDataType + (columnSpecs != null && !columnSpecs.isEmpty()
                ? " " + PlainSelect.getStringList(columnSpecs, false, false)
                : "");
    }

    public EsColumnDefinition withColumnName(String columnName) {
        this.setColumnName(columnName);
        return this;
    }

    public EsColumnDefinition withColDataType(ColDataType colDataType) {
        this.setColDataType(colDataType);
        return this;
    }

    public EsColumnDefinition withColumnSpecs(List<String> columnSpecs) {
        this.setColumnSpecs(columnSpecs);
        return this;
    }

    public EsColumnDefinition addColumnSpecs(String... columnSpecs) {
        List<String> collection = Optional.ofNullable(getColumnSpecs()).orElseGet(ArrayList::new);
        Collections.addAll(collection, columnSpecs);
        return this.withColumnSpecs(collection);
    }

    public EsColumnDefinition addColumnSpecs(Collection<String> columnSpecs) {
        List<String> collection = Optional.ofNullable(getColumnSpecs()).orElseGet(ArrayList::new);
        collection.addAll(columnSpecs);
        return this.withColumnSpecs(collection);
    }
}
