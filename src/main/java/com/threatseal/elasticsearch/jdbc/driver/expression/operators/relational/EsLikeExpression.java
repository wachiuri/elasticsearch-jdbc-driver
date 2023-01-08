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
import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import com.threatseal.elasticsearch.jdbc.driver.querybuilders.EsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class EsLikeExpression extends EsBinaryExpression implements EsQueryBuilder {

    private boolean not = false;
    private Branch escapeExpression = null;
    private boolean caseInsensitive = false;

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean b) {
        not = b;
    }

    @Override
    public String getStringExpression() {
        return caseInsensitive ? "ILIKE" : "LIKE";
    }

    @Override
    public String toString() {
        String retval = getLeftExpression() + " " + (not ? "NOT " : "") + getStringExpression() + " " + getRightExpression();
        if (escapeExpression != null) {
            retval += " ESCAPE " + escapeExpression;
        }

        return retval;
    }

    public Branch getEscape() {
        return escapeExpression;
    }

    public void setEscape(Branch escapeExpression) {
        this.escapeExpression = escapeExpression;
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    public EsLikeExpression withEscape(Branch escape) {
        this.setEscape(escape);
        return this;
    }

    public EsLikeExpression withCaseInsensitive(boolean caseInsensitive) {
        this.setCaseInsensitive(caseInsensitive);
        return this;
    }

    public EsLikeExpression withNot(boolean not) {
        this.setNot(not);
        return this;
    }

    @Override
    public EsLikeExpression withLeftExpression(Branch arg0) {
        return (EsLikeExpression) super.withLeftExpression(arg0);
    }

    @Override
    public EsLikeExpression withRightExpression(Branch arg0) {
        return (EsLikeExpression) super.withRightExpression(arg0);
    }

    @Override
    public QueryBuilder toQueryBuilder() {

        int indexOfPercentage = getRightExpression().toString().indexOf("%");

        System.out.println("index of percentage " + indexOfPercentage);
        System.out.println("length of right expression " + getRightExpression().toString().length());

        if (indexOfPercentage == getRightExpression().toString().length() - 1 && isNot()) {
            return QueryBuilders.boolQuery().mustNot(QueryBuilders.matchPhrasePrefixQuery(getLeftExpression().toString(), getRightExpression().toString().replace("%", "")));
        } else if (indexOfPercentage == getRightExpression().toString().length() - 1) {
            return QueryBuilders.matchPhrasePrefixQuery(getLeftExpression().toString(), getRightExpression().toString().replace("%", ""));
        }

        if (isNot()) {
            return QueryBuilders.boolQuery().mustNot(QueryBuilders.matchQuery(getLeftExpression().toString(), getRightExpression().toString().replace("%", "")));
        }
        return QueryBuilders.matchQuery(getLeftExpression().toString(), getRightExpression().toString().replace("%", ""));
    }

}
