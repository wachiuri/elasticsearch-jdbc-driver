/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.statement;

import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.schema.*;

public class EsAllTableColumns extends BranchImpl implements EsSelectItem {

    private Table table;

    public EsAllTableColumns() {
    }

    public EsAllTableColumns(Table tableName) {
        this.table = tableName;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return table + ".*";
    }

    public EsAllTableColumns withTable(Table table) {
        this.setTable(table);
        return this;
    }
}
