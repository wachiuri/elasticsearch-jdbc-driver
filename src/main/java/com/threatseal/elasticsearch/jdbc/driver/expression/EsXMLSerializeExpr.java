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

import java.util.List;
import static java.util.stream.Collectors.joining;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.select.OrderByElement;

public class EsXMLSerializeExpr extends BranchImpl {

    private Branch expression;
    private List<OrderByElement> orderByElements;
    private ColDataType dataType;

    public Branch getExpression() {
        return expression;
    }

    public void setExpression(Branch expression) {
        this.expression = expression;
    }

    public List<OrderByElement> getOrderByElements() {
        return orderByElements;
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        this.orderByElements = orderByElements;
    }

    public ColDataType getDataType() {
        return dataType;
    }

    public void setDataType(ColDataType dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "xmlserialize(xmlagg(xmltext(" + expression + ")"
                + (orderByElements != null ? " ORDER BY " + orderByElements.stream().map(item -> item.toString()).collect(joining(", ")) : "")
                + ") AS " + dataType + ")";
    }
}
