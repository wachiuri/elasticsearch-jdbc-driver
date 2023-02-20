/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package com.threatseal.elasticsearch.jdbc.driver.sql.jdbc;

import com.threatseal.elasticsearch.jdbc.driver.proto.core.Tuple;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class DefaultCursor implements Cursor {

    private final Logger logger = Logger.getLogger(DefaultCursor.class.getName());
    private final JdbcHttpClient client;
    private final RequestMeta meta;

    private final List<JdbcColumnInfo> columnInfos;
    private List<List<Object>> rows;
    private final List<String> warnings;
    private int row = -1;
    private String cursor;

    DefaultCursor(
            JdbcHttpClient client,
            String cursor,
            List<JdbcColumnInfo> columnInfos,
            List<List<Object>> rows,
            RequestMeta meta,
            List<String> warnings
    ) {
        this.client = client;
        this.meta = meta;
        this.cursor = cursor;
        this.columnInfos = columnInfos;
        this.rows = rows;
        this.warnings = warnings;
    }

    @Override
    public List<JdbcColumnInfo> columns() {
        return columnInfos;
    }

    @Override
    public boolean next() throws SQLException {
        //logger.log(Level.INFO,"DefaultCursor.next");
        if (row < rows.size() - 1) {
            row++;
            return true;
        } else {
            if (cursor.isEmpty() == false) {
                Tuple<String, List<List<Object>>> nextPage = client.nextPage(cursor, meta);
                cursor = nextPage.v1();
                rows = nextPage.v2();
                row = -1;
                return next();
            }
            return false;
        }
    }

    @Override
    public Object column(int column) {
//        logger.log(Level.INFO, "column " + column);
//        logger.log(Level.INFO, rows.get(row).get(column).toString());
        Object object = rows.get(row).get(column);

        if (object == null) {
            return "";
        }

        return object;
    }

    @Override
    public int batchSize() {
        return rows.size();
    }

    @Override
    public void close() throws SQLException {
        if (cursor.isEmpty() == false) {
            client.queryClose(cursor);
        }
    }

    public List<String> warnings() {
        return warnings;
    }

    @Override
    public void clearWarnings() {
        warnings.clear();
    }
}
