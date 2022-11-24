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

import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsItemsList;
import net.sf.jsqlparser.expression.AnyType;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * Combines ANY and SOME expressions.
 *
 * @author toben
 */
public class EsAnyComparisonExpression extends BranchImpl{

    private final EsItemsList itemsList;
    private boolean useBracketsForValues = false;
    private final SubSelect subSelect;
    private final AnyType anyType;

    public EsAnyComparisonExpression(AnyType anyType, SubSelect subSelect) {
        this.anyType = anyType;
        this.subSelect = subSelect;
        this.itemsList = null;
    }

    public EsAnyComparisonExpression(AnyType anyType, EsItemsList itemsList) {
        this.anyType = anyType;
        this.itemsList = itemsList;
        this.subSelect = null;
    }

    public SubSelect getSubSelect() {
        return subSelect;
    }

    public EsItemsList getItemsList() {
        return itemsList;
    }

    public boolean isUsingItemsList() {
        return itemsList!=null;
    }

    public boolean isUsingSubSelect() {
        return subSelect!=null;
    }
    
    public boolean isUsingBracketsForValues() {
        return useBracketsForValues;
    }

    public void setUseBracketsForValues(boolean useBracketsForValues) {
        this.useBracketsForValues = useBracketsForValues;
    }

    public EsAnyComparisonExpression withUseBracketsForValues(boolean useBracketsForValues) {
        this.setUseBracketsForValues(useBracketsForValues);
        return this;
    }
    
    public AnyType getAnyType() {
        return anyType;
    }

    @Override
    public String toString() {
        String s = anyType.name() 
                + " (" 
                + ( subSelect!=null 
                    ? subSelect.toString()
                    : "VALUES " + itemsList.toString())
                + " )";
        return s;
    }
}
