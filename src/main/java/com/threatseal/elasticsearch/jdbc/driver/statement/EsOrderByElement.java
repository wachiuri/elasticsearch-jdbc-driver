/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.statement;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import net.sf.jsqlparser.expression.Expression;

public class EsOrderByElement extends BranchImpl {

    public enum NullOrdering {
        NULLS_FIRST,
        NULLS_LAST
    }

    private Branch expression;
    private boolean asc = true;
    private boolean ascDescPresent = false;
    private NullOrdering nullOrdering;

    public boolean isAsc() {
        return asc;
    }

    public NullOrdering getNullOrdering() {
        return nullOrdering;
    }

    public void setNullOrdering(NullOrdering nullOrdering) {
        this.nullOrdering = nullOrdering;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public void setAscDescPresent(boolean ascDescPresent) {
        this.ascDescPresent = ascDescPresent;
    }

    public boolean isAscDescPresent() {
        return ascDescPresent;
    }

    public Branch getExpression() {
        return expression;
    }

    public void setExpression(Branch expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(expression.toString());

        if (!asc) {
            b.append(" DESC");
        } else if (ascDescPresent) {
            b.append(" ASC");
        }

        if (nullOrdering != null) {
            b.append(' ');
            b.append(nullOrdering == NullOrdering.NULLS_FIRST ? "NULLS FIRST" : "NULLS LAST");
        }
        return b.toString();
    }

    public EsOrderByElement withExpression(Branch expression) {
        this.setExpression(expression);
        return this;
    }

    public EsOrderByElement withAsc(boolean asc) {
        this.setAsc(asc);
        return this;
    }

    public EsOrderByElement withAscDescPresent(boolean ascDescPresent) {
        this.setAscDescPresent(ascDescPresent);
        return this;
    }

    public EsOrderByElement withNullOrdering(NullOrdering nullOrdering) {
        this.setNullOrdering(nullOrdering);
        return this;
    }

    public <E extends Expression> E getExpression(Class<E> type) {
        return type.cast(getExpression());
    }

}
