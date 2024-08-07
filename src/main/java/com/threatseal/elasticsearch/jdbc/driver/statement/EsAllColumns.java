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

public class EsAllColumns extends BranchImpl implements EsSelectItem {

    @Override
    public String toString() {
        return "*";
    }
}
