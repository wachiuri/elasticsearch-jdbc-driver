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
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsExpressionList;
import net.sf.jsqlparser.expression.AnalyticType;
import net.sf.jsqlparser.expression.OrderByClause;
import net.sf.jsqlparser.expression.WindowElement;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.statement.select.OrderByElement;

/**
 *
 * @author tw
 */
public class EsFilterOverImpl extends BranchImpl {
    private final OrderByClause orderBy = new OrderByClause();
    private final EsPartitionByClause partitionBy = new EsPartitionByClause();
    private AnalyticType analyticType = AnalyticType.FILTER_ONLY;
    private Branch filterExpression = null;
    private WindowElement windowElement = null;

    public AnalyticType getAnalyticType() {
        return analyticType;
    }

    public void setAnalyticType(AnalyticType analyticType) {
        this.analyticType = analyticType;
    }
    
    public EsFilterOverImpl withAnalyticType(AnalyticType analyticType) {
        this.setAnalyticType(analyticType);
        return this;
    }

    public List<OrderByElement> getOrderByElements() {
        return orderBy.getOrderByElements();
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        orderBy.setOrderByElements(orderByElements);
    }
    
    public EsFilterOverImpl withOrderByElements(List<OrderByElement> orderByElements) {
        this.setOrderByElements(orderByElements);
        return this;
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

    public boolean isPartitionByBrackets() {
        return partitionBy.isBrackets();
    }
    
    public Branch getFilterExpression() {
        return filterExpression;
    }

    public void setFilterExpression(Branch filterExpression) {
        this.filterExpression = filterExpression;
    }


    public EsFilterOverImpl withFilterExpression(Branch filterExpression) {
        this.setFilterExpression(filterExpression);
        return this;
    }

    public WindowElement getWindowElement() {
        return windowElement;
    }

    public void setWindowElement(WindowElement windowElement) {
        this.windowElement = windowElement;
    }
    
    public EsFilterOverImpl withWindowElement(WindowElement windowElement) {
        this.setWindowElement(windowElement);
        return this;
    }
    
    public void toStringOrderByElements(StringBuilder b) {
        if (orderBy.getOrderByElements()!= null && !orderBy.getOrderByElements().isEmpty()) {
            b.append("ORDER BY ");
            for (int i = 0; i < orderBy.getOrderByElements().size(); i++) {
                if (i > 0) {
                    b.append(", ");
                }
                b.append(orderBy.getOrderByElements().get(i).toString());
            }
        }
    }
    
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity", "PMD.MissingBreakInSwitch"})
    public StringBuilder append(StringBuilder builder) {
        if (filterExpression != null) {
            builder.append("FILTER (WHERE ");
            builder.append(filterExpression.toString());
            builder.append(")");
            if (analyticType != AnalyticType.FILTER_ONLY) {
                builder.append(" ");
            }
        }

        switch (analyticType) {
            case FILTER_ONLY:
                return builder;
            case WITHIN_GROUP:
                builder.append("WITHIN GROUP");
                break;
            default:
                builder.append("OVER");
        }
        builder.append(" (");

        partitionBy.toStringPartitionBy(builder);
        toStringOrderByElements(builder);

        if (windowElement != null) {
            if (orderBy.getOrderByElements() != null) {
                builder.append(' ');
            }
            builder.append(windowElement);
        }

        builder.append(")");

        return builder;
    }

    @Override
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return append(builder).toString();
    }
}
