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

import com.threatseal.elasticsearch.jdbc.driver.querybuilders.EsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * It represents an expression like "(" expression ")"
 */
public class EsParenthesis extends BranchImpl implements EsQueryBuilder {

    @Override
    public QueryBuilder toQueryBuilder() {
        if (this.getChild() instanceof EsQueryBuilder) {
            return ((EsQueryBuilder) getChild()).toQueryBuilder();
        } else {
            throw new IllegalStateException(getChild().getClass().getName()+" in parenthesis "
                    + "is not a query builder");

        }
    }

}
