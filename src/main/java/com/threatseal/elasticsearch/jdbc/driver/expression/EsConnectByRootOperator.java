/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2021 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
/*
 * Copyright (C) 2021 JSQLParser.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */

package com.threatseal.elasticsearch.jdbc.driver.expression;

import com.threatseal.elasticsearch.jdbc.driver.schema.EsColumn;
import java.util.Objects;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import net.sf.jsqlparser.schema.Column;

/**
 *
 * @author are
 */
public class EsConnectByRootOperator extends BranchImpl{
    private final EsColumn column;

    public EsConnectByRootOperator(EsColumn column) {
        this.column = Objects.requireNonNull(column, "The COLUMN of the ConnectByRoot Operator must not be null");
    }

    public EsColumn getColumn() {
        return column;
    }
    
    public StringBuilder appendTo(StringBuilder builder) {
        builder.append("CONNECT_BY_ROOT ").append(column);
        return builder;
    }
    
    @Override
    public String toString() {
        return appendTo(new StringBuilder()).toString();
    }

}