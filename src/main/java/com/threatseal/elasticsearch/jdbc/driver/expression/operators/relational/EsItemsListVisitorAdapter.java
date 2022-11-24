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

import net.sf.jsqlparser.statement.select.SubSelect;

@SuppressWarnings({"PMD.UncommentedEmptyMethodBody"})
public class EsItemsListVisitorAdapter implements EsItemsListVisitor {

    @Override
    public void visit(SubSelect subSelect) {

    }

    @Override
    public void visit(EsNamedExpressionList namedExpressionList) {

    }

    @Override
    public void visit(EsExpressionList expressionList) {

    }

    @Override
    public void visit(EsMultiExpressionList multiExprList) {
        for (EsExpressionList list : multiExprList.getExprList()) {
            visit(list);
        }
    }
}
