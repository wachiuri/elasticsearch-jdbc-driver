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

/**
 * A list of named expressions, as in
 * as in select substr('xyzzy' from 2 for 3)
 */
public class EsNamedExpressionList extends BranchImpl implements EsItemsList {

    private List<Branch> expressions=new ArrayList<>();
    private List<String> names;

    public EsNamedExpressionList() {
    }

    public EsNamedExpressionList(List<Branch> expressions) {
        this.expressions = expressions;
    }

    public EsNamedExpressionList(Branch... expressions) {
        this.expressions = Arrays.asList(expressions);
    }

    public List<Branch> getExpressions() {
        return expressions;
    }

    public List<String> getNames() {
        return names;
    }

    public void setExpressions(List<Branch> list) {
        expressions = list;
    }

    public void setNames(List<String> list) {
        names = list;
    }

    @Override
    public String toString() {

        StringBuilder ret = new StringBuilder();
        ret.append("(");
        for (int i = 0; i < expressions.size(); i++) {
            if (i > 0) {
                ret.append(" ");
            }
            if (!names.get(i).equals("")) {
                ret.append(names.get(i)).append(" ").append(expressions.get(i));
            } else {
                ret.append(expressions.get(i));
            }
        }
        ret.append(")");

        return ret.toString();
    }

    public EsNamedExpressionList withExpressions(List<Branch> expressions) {
        this.setExpressions(expressions);
        return this;
    }

    public EsNamedExpressionList withNames(List<String> names) {
        this.setNames(names);
        return this;
    }

    public EsNamedExpressionList addExpressions(Branch... expressions) {
        List<Branch> collection = Optional.ofNullable(getExpressions()).orElseGet(ArrayList::new);
        Collections.addAll(collection, expressions);
        return this.withExpressions(collection);
    }

    public EsNamedExpressionList addExpressions(Collection<? extends Branch> expressions) {
        List<Branch> collection = Optional.ofNullable(getExpressions()).orElseGet(ArrayList::new);
        collection.addAll(expressions);
        return this.withExpressions(collection);
    }

    public EsNamedExpressionList addNames(String... names) {
        List<String> collection = Optional.ofNullable(getNames()).orElseGet(ArrayList::new);
        Collections.addAll(collection, names);
        return this.withNames(collection);
    }

    public EsNamedExpressionList addNames(Collection<String> names) {
        List<String> collection = Optional.ofNullable(getNames()).orElseGet(ArrayList::new);
        collection.addAll(names);
        return this.withNames(collection);
    }
}
