/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.schema;

import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import java.util.List;
import net.sf.jsqlparser.schema.MultiPartName;
import net.sf.jsqlparser.schema.Table;

/**
 *
 * @author Timothy Wachiuri
 */
public class EsColumn extends BranchImpl implements MultiPartName {

    private String columnName;
    private Table table;

    public EsColumn() {
    }

    public EsColumn(Table table, String columnName) {
        setTable(table);
        setColumnName(columnName);
    }

    public EsColumn(List<String> nameParts) {
        this(nameParts.size() > 1 ? new Table(nameParts.subList(0, nameParts.size() - 1)) : null,
                nameParts.get(nameParts.size() - 1));
    }

    public EsColumn(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * Retrieve the information regarding the {@code Table} this {@code Column}
     * does belong to, if any can be inferred.
     * <p>
     * The inference is based only on local information, and not on the whole
     * SQL command. For example, consider the following query:
     * <blockquote><pre>
     *  SELECT x FROM Foo
     * </pre></blockquote>
     * Given the {@code Column} called {@code x}, this method would return
     * {@code null}, and not the info about the table {@code Foo}. On the other
     * hand, consider:
     * <blockquote><pre>
     *  SELECT t.x FROM Foo t
     * </pre></blockquote>
     * Here, we will get a {@code Table} object for a table called {@code t}.
     * But because the inference is local, such object will not know that
     * {@code t} is just an alias for {@code Foo}.
     *
     * @return an instance of {@link net.sf.jsqlparser.schema.Table}
     * representing the table this column does belong to, if it can be inferred.
     * Can be {@code null}.
     */
    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    @Override
    public String getFullyQualifiedName() {
        return getName(false);
    }

    public String getName(boolean aliases) {
        StringBuilder fqn = new StringBuilder();

        if (table != null) {
            if (table.getAlias() != null && aliases) {
                fqn.append(table.getAlias().getName());
            } else {
                fqn.append(table.getFullyQualifiedName());
            }
        }
        if (fqn.length() > 0) {
            fqn.append('.');
        }
        if (columnName != null) {
            fqn.append(columnName);
        }
        return fqn.toString();
    }

    @Override
    public String toString() {
        return getName(true).replace("`", "");
    }

    public EsColumn withTable(Table table) {
        this.setTable(table);
        return this;
    }

    public EsColumn withColumnName(String columnName) {
        this.setColumnName(columnName);
        return this;
    }

    @Override
    public Object toObject() {
        return toString();
    }

}
