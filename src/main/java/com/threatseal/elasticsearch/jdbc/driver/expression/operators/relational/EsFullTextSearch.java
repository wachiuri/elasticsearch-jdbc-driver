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
import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsJdbcNamedParameter;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsJdbcParameter;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsStringValue;
import com.threatseal.elasticsearch.jdbc.driver.schema.EsColumn;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class EsFullTextSearch extends BranchImpl {

    private List<EsColumn> _matchColumns = new ArrayList<>();
    private Branch _againstValue;
    private String _searchModifier;

    public EsFullTextSearch() {

    }

    public void setMatchColumns(List<EsColumn> columns) {
        this._matchColumns = columns;
    }

    public List<EsColumn> getMatchColumns() {
        return this._matchColumns;
    }

    public void setAgainstValue(EsStringValue val) {
        this._againstValue = val;
    }

    public void setAgainstValue(EsJdbcNamedParameter val) {
        this._againstValue = val;
    }

    public void setAgainstValue(EsJdbcParameter val) {
        this._againstValue = val;
    }

    public Branch getAgainstValue() {
        return this._againstValue;
    }

    public void setSearchModifier(String val) {
        this._searchModifier = val;
    }

    public String getSearchModifier() {
        return this._searchModifier;
    }

    @Override
    public String toString() {
        // Build a list of matched columns
        String columnsListCommaSeperated = "";
        Iterator<EsColumn> iterator = this._matchColumns.iterator();
        while (iterator.hasNext()) {
            EsColumn col = iterator.next();
            columnsListCommaSeperated += col.getFullyQualifiedName();
            if (iterator.hasNext()) {
                columnsListCommaSeperated += ",";
            }
        }

        return "MATCH (" + columnsListCommaSeperated + ") AGAINST (" + this._againstValue
                + (this._searchModifier != null ? " " + this._searchModifier : "") + ")";
    }

    public EsFullTextSearch withMatchColumns(List<EsColumn> matchColumns) {
        this.setMatchColumns(matchColumns);
        return this;
    }

    public EsFullTextSearch withAgainstValue(EsStringValue againstValue) {
        this.setAgainstValue(againstValue);
        return this;
    }

    public EsFullTextSearch withSearchModifier(String searchModifier) {
        this.setSearchModifier(searchModifier);
        return this;
    }

    public EsFullTextSearch addMatchColumns(EsColumn... matchColumns) {
        List<EsColumn> collection = Optional.ofNullable(getMatchColumns()).orElseGet(ArrayList::new);
        Collections.addAll(collection, matchColumns);
        return this.withMatchColumns(collection);
    }

    public EsFullTextSearch addMatchColumns(Collection<? extends EsColumn> matchColumns) {
        List<EsColumn> collection = Optional.ofNullable(getMatchColumns()).orElseGet(ArrayList::new);
        collection.addAll(matchColumns);
        return this.withMatchColumns(collection);
    }
}
