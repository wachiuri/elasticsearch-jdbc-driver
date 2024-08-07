/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package com.threatseal.elasticsearch.jdbc.driver.sql.jdbc;

final class ResultSetMetaDataProxy extends DebuggingInvoker {

    ResultSetMetaDataProxy(DebugLog log, Object target) {
        super(log, target, null);
    }
}
