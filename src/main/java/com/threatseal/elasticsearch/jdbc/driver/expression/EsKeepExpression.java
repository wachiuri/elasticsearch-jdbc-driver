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

import com.threatseal.elasticsearch.jdbc.driver.statement.EsOrderByElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.statement.select.OrderByElement;

public class EsKeepExpression extends BranchImpl {

    private String name;
    private List<EsOrderByElement> orderByElements = new ArrayList<>();
    private boolean first = false;

    public List<EsOrderByElement> getOrderByElements() {
        return orderByElements;
    }

    public void setOrderByElements(List<EsOrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        b.append("KEEP (").append(name);

        b.append(" ").append(first ? "FIRST" : "LAST").append(" ");
        toStringOrderByElements(b);

        b.append(")");

        return b.toString();
    }

    private void toStringOrderByElements(StringBuilder b) {
        if (orderByElements != null && !orderByElements.isEmpty()) {
            b.append("ORDER BY ");
            for (int i = 0; i < orderByElements.size(); i++) {
                if (i > 0) {
                    b.append(", ");
                }
                b.append(orderByElements.get(i).toString());
            }
        }
    }

    public EsKeepExpression withName(String name) {
        this.setName(name);
        return this;
    }

    public EsKeepExpression withOrderByElements(List<EsOrderByElement> orderByElements) {
        this.setOrderByElements(orderByElements);
        return this;
    }

    public EsKeepExpression withFirst(boolean first) {
        this.setFirst(first);
        return this;
    }

    public EsKeepExpression addOrderByElements(EsOrderByElement... orderByElements) {
        List<EsOrderByElement> collection = Optional.ofNullable(getOrderByElements()).orElseGet(ArrayList::new);
        Collections.addAll(collection, orderByElements);
        return this.withOrderByElements(collection);
    }

    public EsKeepExpression addOrderByElements(Collection<? extends EsOrderByElement> orderByElements) {
        List<EsOrderByElement> collection = Optional.ofNullable(getOrderByElements()).orElseGet(ArrayList::new);
        collection.addAll(orderByElements);
        return this.withOrderByElements(collection);
    }
}
