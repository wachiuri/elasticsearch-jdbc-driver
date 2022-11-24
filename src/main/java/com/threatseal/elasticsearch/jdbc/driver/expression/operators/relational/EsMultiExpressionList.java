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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.sf.jsqlparser.expression.Expression;

/**
 * A list of ExpressionList items. e.g. multi values of insert statements. This
 * one allows only equally sized ExpressionList.
 */
public class EsMultiExpressionList extends BranchImpl implements EsItemsList {

    private List<EsExpressionList> expressionLists = new ArrayList<>();

    public EsMultiExpressionList() {
        this.expressionLists = new ArrayList<>();
    }

    @Deprecated
    public List<EsExpressionList> getExprList() {
        return getExpressionLists();
    }

    public List<EsExpressionList> getExpressionLists() {
        return expressionLists;
    }

    public void setExpressionLists(List<EsExpressionList> expressionLists) {
        this.expressionLists = expressionLists;
    }

    public EsMultiExpressionList withExpressionLists(List<EsExpressionList> expressionLists) {
        this.setExpressionLists(expressionLists);
        return this;
    }

    public void addExpressionList(EsExpressionList el) {
        if (!expressionLists.isEmpty()
                && expressionLists.get(0).getExpressions().size() != el.getExpressions().size()) {
            throw new IllegalArgumentException("different count of parameters");
        }
        expressionLists.add(el);
    }

    public void addExpressionList(List<Branch> list) {
        addExpressionList(new EsExpressionList(list));
    }

    public void addExpressionList(Branch... expr) {
        addExpressionList(new EsExpressionList(Arrays.asList(expr)));
    }

    public EsMultiExpressionList addExpressionLists(EsExpressionList... expr) {
        Stream.of(expr).forEach(this::addExpressionList);
        return this;
    }

    public EsMultiExpressionList addExpressionLists(Collection<? extends EsExpressionList> expr) {
        expr.stream().forEach(this::addExpressionList);
        return this;
    }

    @Override
    public String toString() {
        return expressionLists.stream().map(EsExpressionList::toString).collect(Collectors.joining(", "));
    }
}
