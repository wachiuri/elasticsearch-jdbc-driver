/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.statement.select;

import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import com.threatseal.elasticsearch.jdbc.driver.schema.EsTable;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.schema.*;

public class AllTableColumns extends BranchImpl {

    private EsTable table;

    public AllTableColumns() {
    }

    public AllTableColumns(EsTable tableName) {
        this.table = tableName;
    }

    public EsTable getTable() {
        return table;
    }

    public void setTable(EsTable table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return table + ".*";
    }

    public AllTableColumns withTable(EsTable table) {
        this.setTable(table);
        return this;
    }
}
