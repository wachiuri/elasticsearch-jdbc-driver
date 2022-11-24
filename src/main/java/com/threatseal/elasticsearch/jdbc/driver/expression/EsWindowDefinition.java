/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2022 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression;

import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsExpressionList;
import java.util.List;
import net.sf.jsqlparser.expression.OrderByClause;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.expression.PartitionByClause;
import net.sf.jsqlparser.expression.WindowElement;

public class EsWindowDefinition {

    final OrderByClause orderBy = new OrderByClause();
    final EsPartitionByClause partitionBy = new EsPartitionByClause();
    WindowElement windowElement = null;
    private String windowName;

    public WindowElement getWindowElement() {
        return windowElement;
    }

    public void setWindowElement(WindowElement windowElement) {
        this.windowElement = windowElement;
    }

    public List<OrderByElement> getOrderByElements() {
        return orderBy.getOrderByElements();
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        orderBy.setOrderByElements(orderByElements);
    }

    public EsExpressionList getPartitionExpressionList() {
        return partitionBy.getPartitionExpressionList();
    }

    public void setPartitionExpressionList(EsExpressionList partitionExpressionList) {
        setPartitionExpressionList(partitionExpressionList, false);
    }

    public void setPartitionExpressionList(EsExpressionList partitionExpressionList, boolean brackets) {
        partitionBy.setPartitionExpressionList(partitionExpressionList, brackets);
    }

    public String getWindowName() {
        return windowName;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    public EsWindowDefinition withWindowName(String windowName) {
        setWindowName(windowName);
        return this;
    }
}
