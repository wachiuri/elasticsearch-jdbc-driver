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

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

public class EsJsonExpression extends BranchImpl {

    private Branch expr;

    private List<String> idents = new ArrayList<String>();
    private List<String> operators = new ArrayList<String>();

    public Branch getExpression() {
        return expr;
    }

    public void setExpression(Branch expr) {
        this.expr = expr;
    }

    public List<String> getIdents() {
        return idents;
    }

    public void setIdents(List<String> idents) {
        this.idents = idents;
        operators = new ArrayList<String>();
        for (String ident : idents) {
            operators.add("->");
        }
    }

    public void addIdent(String ident) {
        addIdent(ident, "->");
    }

    public void addIdent(String ident, String operator) {
        idents.add(ident);
        operators.add(operator);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(expr.toString());
        for (int i = 0; i < idents.size(); i++) {
            b.append(operators.get(i)).append(idents.get(i));
        }
        return b.toString();
    }

    public EsJsonExpression withExpression(Branch expr) {
        this.setExpression(expr);
        return this;
    }

    public EsJsonExpression withIdents(List<String> idents) {
        this.setIdents(idents);
        return this;
    }
}
