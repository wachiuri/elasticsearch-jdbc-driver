/* -
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression;

import java.util.List;
import java.util.regex.Pattern;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

public class EsNextValExpression extends BranchImpl {

    public static final Pattern NEXT_VALUE_PATTERN = Pattern.compile("NEXT\\s+VALUE\\s+FOR", Pattern.CASE_INSENSITIVE);
    private final List<String> nameList;
    private boolean usingNextValueFor = false;

    public EsNextValExpression(List<String> nameList) {
        this.nameList = nameList;
    }

    public boolean isUsingNextValueFor() {
        return usingNextValueFor;
    }

    public void setUsingNextValueFor(boolean usingNextValueFor) {
        this.usingNextValueFor = usingNextValueFor;
    }

    public EsNextValExpression withNextValueFor(boolean usingNextValueFor) {
        setUsingNextValueFor(usingNextValueFor);
        return this;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public String getName() {
        StringBuilder b = new StringBuilder();
        for (String name : nameList) {
            if (b.length() > 0) {
                b.append(".");
            }
            b.append(name);
        }
        return b.toString();
    }

    @Override
    public String toString() {
        return (usingNextValueFor
                ? "NEXT VALUE FOR "
                : "NEXTVAL FOR ") + getName();
    }
}