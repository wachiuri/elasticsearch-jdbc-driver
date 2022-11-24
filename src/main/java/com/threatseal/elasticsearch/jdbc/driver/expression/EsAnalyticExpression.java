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

import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsExpressionList;
import com.threatseal.elasticsearch.jdbc.driver.statement.EsOrderByElement;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.joining;
import net.sf.jsqlparser.expression.AnalyticType;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.WindowElement;
//import net.sf.jsqlparser.expression.W;

/**
 * Analytic function. The name of the function is variable but the parameters
 * following the special analytic function path. e.g. row_number() over (order
 * by test). Additional there can be an expression for an analytical aggregate
 * like sum(col) or the "all collumns" wildcard like count(*).
 *
 * @author tw
 */
public class EsAnalyticExpression extends BranchImpl {

    private String name;
    private Branch expression;
    private Branch offset;
    private Branch defaultValue;
    private boolean allColumns = false;
    private EsKeepExpression keep = null;
    private AnalyticType type = AnalyticType.OVER;
    private boolean distinct = false;
    private boolean unique = false;
    private boolean ignoreNulls = false;            //IGNORE NULLS inside function parameters
    private boolean ignoreNullsOutside = false;     //IGNORE NULLS outside function parameters
    private Branch filterExpression = null;
    private List<EsOrderByElement> funcOrderBy = new ArrayList<>();
    private String windowName = null;               // refers to an external window definition (paritionBy, orderBy, windowElement)
    private EsWindowDefinition windowDef = new EsWindowDefinition();

    public EsAnalyticExpression() {
    }

    public EsAnalyticExpression(EsFunction function) {
        name = function.getName();
        allColumns = function.isAllColumns();
        distinct = function.isDistinct();
        unique = function.isUnique();
        funcOrderBy = function.getOrderByElements();

        EsExpressionList list = function.getParameters();
        if (list != null) {
            if (list.getExpressions().size() > 3) {
                throw new IllegalArgumentException("function object not valid to initialize analytic expression");
            }

            expression = list.getExpressions().get(0);
            if (list.getExpressions().size() > 1) {
                offset = list.getExpressions().get(1);
            }
            if (list.getExpressions().size() > 2) {
                defaultValue = list.getExpressions().get(2);
            }
        }
        ignoreNulls = function.isIgnoreNulls();
        keep = function.getKeep();
    }

    public List<OrderByElement> getOrderByElements() {
        return windowDef.orderBy.getOrderByElements();
    }

    public void setOrderByElements(List<OrderByElement> orderByElements) {
        windowDef.orderBy.setOrderByElements(orderByElements);
    }

    public EsKeepExpression getKeep() {
        return keep;
    }

    public void setKeep(EsKeepExpression keep) {
        this.keep = keep;
    }

    public EsExpressionList getPartitionExpressionList() {
        return windowDef.partitionBy.getPartitionExpressionList();
    }

    public void setPartitionExpressionList(EsExpressionList partitionExpressionList) {
        setPartitionExpressionList(partitionExpressionList, false);
    }

    public void setPartitionExpressionList(EsExpressionList partitionExpressionList, boolean brackets) {
        windowDef.partitionBy.setPartitionExpressionList(partitionExpressionList, brackets);
    }

    public boolean isPartitionByBrackets() {
        return windowDef.partitionBy.isBrackets();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Branch getExpression() {
        return expression;
    }

    public void setExpression(Branch expression) {
        this.expression = expression;
    }

    public Branch getOffset() {
        return offset;
    }

    public void setOffset(Branch offset) {
        this.offset = offset;
    }

    public Branch getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Branch defaultValue) {
        this.defaultValue = defaultValue;
    }

    public WindowElement getWindowElement() {
        return windowDef.windowElement;
    }

    public void setWindowElement(WindowElement windowElement) {
        windowDef.windowElement = windowElement;
    }

    public AnalyticType getType() {
        return type;
    }

    public void setType(AnalyticType type) {
        this.type = type;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isIgnoreNulls() {
        return ignoreNulls;
    }

    public void setIgnoreNulls(boolean ignoreNulls) {
        this.ignoreNulls = ignoreNulls;
    }

    public boolean isIgnoreNullsOutside() {
        return ignoreNullsOutside;
    }

    public void setIgnoreNullsOutside(boolean ignoreNullsOutside) {
        this.ignoreNullsOutside = ignoreNullsOutside;
    }

    public String getWindowName() {
        return windowName;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    public EsWindowDefinition getWindowDefinition() {
        return windowDef;
    }

    public void setWindowDefinition(EsWindowDefinition windowDef) {
        this.windowDef = windowDef;
    }

    @Override
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity", "PMD.MissingBreakInSwitch"})
    public String toString() {
        StringBuilder b = new StringBuilder();

        b.append(name).append("(");
        if (isDistinct()) {
            b.append("DISTINCT ");
        }
        if (expression != null) {
            b.append(expression.toString());
            if (offset != null) {
                b.append(", ").append(offset.toString());
                if (defaultValue != null) {
                    b.append(", ").append(defaultValue.toString());
                }
            }
        } else if (isAllColumns()) {
            b.append("*");
        }
        if (isIgnoreNulls()) {
            b.append(" IGNORE NULLS");
        }
        if (funcOrderBy != null) {
            b.append(" ORDER BY ");
            b.append(funcOrderBy.stream().map(OrderByElement::toString).collect(joining(", ")));
        }

        b.append(") ");
        if (keep != null) {
            b.append(keep.toString()).append(" ");
        }

        if (filterExpression != null) {
            b.append("FILTER (WHERE ");
            b.append(filterExpression.toString());
            b.append(")");
            if (type != AnalyticType.FILTER_ONLY) {
                b.append(" ");
            }
        }

        if (isIgnoreNullsOutside()) {
            b.append("IGNORE NULLS ");
        }

        switch (type) {
            case FILTER_ONLY:
                return b.toString();
            case WITHIN_GROUP:
                b.append("WITHIN GROUP");
                break;
            default:
                b.append("OVER");
        }

        if (windowName != null) {
            b.append(" ").append(windowName);
        } else {
            b.append(" ");
            b.append(windowDef.toString());
        }

        return b.toString();
    }

    public boolean isAllColumns() {
        return allColumns;
    }

    public void setAllColumns(boolean allColumns) {
        this.allColumns = allColumns;
    }

    public Branch getFilterExpression() {
        return filterExpression;
    }

    public void setFilterExpression(Branch filterExpression) {
        this.filterExpression = filterExpression;
    }

    public EsAnalyticExpression withName(String name) {
        this.setName(name);
        return this;
    }

    public EsAnalyticExpression withExpression(Branch expression) {
        this.setExpression(expression);
        return this;
    }

    public EsAnalyticExpression withOffset(Branch offset) {
        this.setOffset(offset);
        return this;
    }

    public EsAnalyticExpression withDefaultValue(Branch defaultValue) {
        this.setDefaultValue(defaultValue);
        return this;
    }

    public EsAnalyticExpression withAllColumns(boolean allColumns) {
        this.setAllColumns(allColumns);
        return this;
    }

    public EsAnalyticExpression withKeep(EsKeepExpression keep) {
        this.setKeep(keep);
        return this;
    }

    public EsAnalyticExpression withType(AnalyticType type) {
        this.setType(type);
        return this;
    }

    public EsAnalyticExpression withDistinct(boolean distinct) {
        this.setDistinct(distinct);
        return this;
    }

    public EsAnalyticExpression withUnique(boolean unique) {
        this.setUnique(unique);
        return this;
    }

    public EsAnalyticExpression withIgnoreNulls(boolean ignoreNulls) {
        this.setIgnoreNulls(ignoreNulls);
        return this;
    }

    public EsAnalyticExpression withFilterExpression(Branch filterExpression) {
        this.setFilterExpression(filterExpression);
        return this;
    }

    public EsAnalyticExpression withWindowElement(WindowElement windowElement) {
        this.setWindowElement(windowElement);
        return this;
    }

    public <E extends Branch> E getExpression(Class<E> type) {
        return type.cast(getExpression());
    }

    public <E extends Branch> E getOffset(Class<E> type) {
        return type.cast(getOffset());
    }

    public <E extends Branch> E getDefaultValue(Class<E> type) {
        return type.cast(getDefaultValue());
    }

    public <E extends Branch> E getFilterExpression(Class<E> type) {
        return type.cast(getFilterExpression());
    }

    public List<EsOrderByElement> getFuncOrderBy() {
        return funcOrderBy;
    }

    public void setFuncOrderBy(List<EsOrderByElement> funcOrderBy) {
        this.funcOrderBy = funcOrderBy;
    }
}
