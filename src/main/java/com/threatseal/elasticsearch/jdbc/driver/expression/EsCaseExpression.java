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
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * CASE/WHEN expression.
 *
 * Syntax:
 * <pre><code>
 * CASE
 * WHEN condition THEN expression
 * [WHEN condition THEN expression]...
 * [ELSE expression]
 * END
 * </code></pre>
 *
 * <br>
 * or <br>
 * <br>
 *
 * <pre><code>
 * CASE expression
 * WHEN condition THEN expression
 * [WHEN condition THEN expression]...
 * [ELSE expression]
 * END
 * </code></pre>
 *
 */
public class EsCaseExpression extends BranchImpl {

    private boolean usingBrackets = false;
    private Branch switchExpression;
    private List<EsWhenClause> whenClauses = new ArrayList<>();
    private Branch elseExpression;

    public Branch getSwitchExpression() {
        return switchExpression;
    }

    public void setSwitchExpression(Branch switchExpression) {
        this.switchExpression = switchExpression;
    }

    /**
     * @return Returns the elseExpression.
     */
    public Branch getElseExpression() {
        return elseExpression;
    }

    /**
     * @param elseExpression The elseExpression to set.
     */
    public void setElseExpression(Branch elseExpression) {
        this.elseExpression = elseExpression;
    }

    /**
     * @return Returns the whenClauses.
     */
    public List<EsWhenClause> getWhenClauses() {
        return whenClauses;
    }

    /**
     * @param whenClauses The whenClauses to set.
     */
    public void setWhenClauses(List<EsWhenClause> whenClauses) {
        this.whenClauses = whenClauses;
    }

    @Override
    public String toString() {
        return (usingBrackets ? "(" : "") + "CASE " + ((switchExpression != null) ? switchExpression + " " : "")
                + PlainSelect.getStringList(whenClauses, false, false) + " "
                + ((elseExpression != null) ? "ELSE " + elseExpression + " " : "") + "END" + (usingBrackets ? ")" : "");
    }

    public EsCaseExpression withSwitchExpression(Branch switchExpression) {
        this.setSwitchExpression(switchExpression);
        return this;
    }

    public EsCaseExpression withWhenClauses(List<EsWhenClause> whenClauses) {
        this.setWhenClauses(whenClauses);
        return this;
    }

    public EsCaseExpression withElseExpression(Branch elseExpression) {
        this.setElseExpression(elseExpression);
        return this;
    }

    public EsCaseExpression addWhenClauses(EsWhenClause... whenClauses) {
        List<EsWhenClause> collection = Optional.ofNullable(getWhenClauses()).orElseGet(ArrayList::new);
        Collections.addAll(collection, whenClauses);
        return this.withWhenClauses(collection);
    }

    public EsCaseExpression addWhenClauses(Collection<? extends EsWhenClause> whenClauses) {
        List<EsWhenClause> collection = Optional.ofNullable(getWhenClauses()).orElseGet(ArrayList::new);
        collection.addAll(whenClauses);
        return this.withWhenClauses(collection);
    }

    public <E extends Branch> E getSwitchExpression(Class<E> type) {
        return type.cast(getSwitchExpression());
    }

    public <E extends Branch> E getElseExpression(Class<E> type) {
        return type.cast(getElseExpression());
    }

    /**
     * @return the usingBrackets
     */
    public boolean isUsingBrackets() {
        return usingBrackets;
    }

    /**
     * @param usingBrackets the usingBrackets to set
     */
    public void setUsingBrackets(boolean usingBrackets) {
        this.usingBrackets = usingBrackets;
    }

    /**
     * @param usingBrackets the usingBrackets to set
     */
    public EsCaseExpression withUsingBrackets(boolean usingBrackets) {
        this.usingBrackets = usingBrackets;
        return this;
    }
}
