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
import com.threatseal.elasticsearch.jdbc.driver.querybuilders.EsQueryBuilder;
import com.threatseal.elasticsearch.jdbc.driver.schema.EsColumn;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class EsFullTextSearch extends BranchImpl implements EsQueryBuilder {

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

        return toObject().toString();
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

    @Override
    public Object toObject() {
        return new Object();
    }

    @Override
    public QueryBuilder toQueryBuilder() {

        if (_matchColumns.size() > 1) {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

            for (EsColumn column : _matchColumns) {
                queryBuilder.must(QueryBuilders.matchPhraseQuery(column.getColumnName(), _againstValue));
            }
            return queryBuilder;

        } else {
            MatchPhraseQueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery(_matchColumns.get(0).getColumnName(), _againstValue);
            return queryBuilder;
        }
    }
}
