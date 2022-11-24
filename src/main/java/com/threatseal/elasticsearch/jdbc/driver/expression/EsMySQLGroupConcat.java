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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsExpressionList;
import com.threatseal.elasticsearch.jdbc.driver.statement.EsOrderByElement;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class EsMySQLGroupConcat extends BranchImpl {

    private EsExpressionList expressionList = new EsExpressionList();
    private boolean distinct = false;
    private List<EsOrderByElement> orderByElements = new ArrayList<>();
    private String separator;

    public EsExpressionList getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(EsExpressionList expressionList) {
        this.expressionList = expressionList;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public List<EsOrderByElement> getOrderByElements() {
        return orderByElements;
    }

    public void setOrderByElements(List<EsOrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("GROUP_CONCAT(");
        if (isDistinct()) {
            b.append("DISTINCT ");
        }
        b.append(PlainSelect.getStringList(expressionList.getExpressions(), true, false));
        if (orderByElements != null && !orderByElements.isEmpty()) {
            b.append(" ORDER BY ");
            for (int i = 0; i < orderByElements.size(); i++) {
                if (i > 0) {
                    b.append(", ");
                }
                b.append(orderByElements.get(i).toString());
            }
        }
        if (separator != null) {
            b.append(" SEPARATOR ").append(separator);
        }
        b.append(")");
        return b.toString();
    }

    public EsMySQLGroupConcat withExpressionList(EsExpressionList expressionList) {
        this.setExpressionList(expressionList);
        return this;
    }

    public EsMySQLGroupConcat withDistinct(boolean distinct) {
        this.setDistinct(distinct);
        return this;
    }

    public EsMySQLGroupConcat withOrderByElements(List<EsOrderByElement> orderByElements) {
        this.setOrderByElements(orderByElements);
        return this;
    }

    public EsMySQLGroupConcat withSeparator(String separator) {
        this.setSeparator(separator);
        return this;
    }

    public EsMySQLGroupConcat addOrderByElements(EsOrderByElement... orderByElements) {
        List<EsOrderByElement> collection = Optional.ofNullable(getOrderByElements()).orElseGet(ArrayList::new);
        Collections.addAll(collection, orderByElements);
        return this.withOrderByElements(collection);
    }

    public EsMySQLGroupConcat addOrderByElements(Collection<? extends EsOrderByElement> orderByElements) {
        List<EsOrderByElement> collection = Optional.ofNullable(getOrderByElements()).orElseGet(ArrayList::new);
        collection.addAll(orderByElements);
        return this.withOrderByElements(collection);
    }
}
