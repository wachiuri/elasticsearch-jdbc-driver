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
import net.sf.jsqlparser.statement.select.PlainSelect;

public class EsPartitionByClause {
    EsExpressionList partitionExpressionList;
    boolean brackets = false;

    public EsExpressionList getPartitionExpressionList() {
        return partitionExpressionList;
    }
    
    public void setPartitionExpressionList(EsExpressionList partitionExpressionList) {
        setPartitionExpressionList(partitionExpressionList, false);
    }

    public void setPartitionExpressionList(EsExpressionList partitionExpressionList, boolean brackets) {
        this.partitionExpressionList = partitionExpressionList;
        this.brackets = brackets;
    }

    void toStringPartitionBy(StringBuilder b) {
        if (partitionExpressionList != null && !partitionExpressionList.getExpressions().isEmpty()) {
            b.append("PARTITION BY ");
            b.append(PlainSelect.
                    getStringList(partitionExpressionList.getExpressions(), true, brackets));
            b.append(" ");
        }
    }
    
    public boolean isBrackets() {
        return brackets;
    }

    public EsPartitionByClause withPartitionExpressionList(EsExpressionList partitionExpressionList) {
        this.setPartitionExpressionList(partitionExpressionList);
        return this;
    }
}
