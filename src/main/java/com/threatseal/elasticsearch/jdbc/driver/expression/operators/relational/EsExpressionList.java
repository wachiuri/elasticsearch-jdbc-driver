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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * A list of expressions, as in SELECT A FROM TAB WHERE B IN (expr1,expr2,expr3)
 */
public class EsExpressionList extends BranchImpl {

    private List<Branch> expressions = new ArrayList<>();
    private boolean usingBrackets = true;

    public boolean isUsingBrackets() {
        return usingBrackets;
    }

    public void setUsingBrackets(boolean usingBrackets) {
        this.usingBrackets = usingBrackets;
    }

    public EsExpressionList withUsingBrackets(boolean usingBrackets) {
        setUsingBrackets(usingBrackets);
        return this;
    }

    public EsExpressionList() {
    }

    public EsExpressionList(List<Branch> expressions) {
        this.expressions = expressions;
    }

    public EsExpressionList(Branch... expressions) {
        this.expressions = new ArrayList<>(Arrays.asList(expressions));
    }

    public List<Branch> getExpressions() {
        return expressions;
    }

    public EsExpressionList addExpressions(Branch... elements) {
        List<Branch> list = Optional.ofNullable(getExpressions()).orElseGet(ArrayList::new);
        Collections.addAll(list, elements);
        return withExpressions(list);
    }

    public EsExpressionList withExpressions(List<Branch> expressions) {
        this.setExpressions(expressions);
        return this;
    }

    public void setExpressions(List<Branch> expressions) {
        this.expressions = expressions;
    }

    @Deprecated
    public EsExpressionList withBrackets(boolean brackets) {
        return withUsingBrackets(brackets);
    }

    @Override
    public String toString() {
        return PlainSelect.getStringList(expressions, true, usingBrackets);
    }

    public EsExpressionList addExpressions(Collection<? extends Branch> expressions) {
        List<Branch> collection = Optional.ofNullable(getExpressions()).orElseGet(ArrayList::new);
        collection.addAll(expressions);
        return this.withExpressions(collection);
    }
}
