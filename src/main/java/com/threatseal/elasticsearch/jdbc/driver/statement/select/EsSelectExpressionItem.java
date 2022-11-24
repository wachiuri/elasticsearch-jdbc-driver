/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.statement.select;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;

/**
 *
 * @author Timothy Wachiuri
 */
public class EsSelectExpressionItem extends BranchImpl {

    private Alias alias;
    private Branch expression;

    public EsSelectExpressionItem() {
    }

    public EsSelectExpressionItem(Branch expression) {
        this.expression = expression;
    }

    public Alias getAlias() {
        return alias;
    }

    public Branch getExpression() {
        return expression;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    public void setExpression(Branch expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return expression + ((alias != null) ? alias.toString() : "");
    }

    public EsSelectExpressionItem withExpression(Branch expression) {
        this.setExpression(expression);
        return this;
    }

    public EsSelectExpressionItem withAlias(Alias alias) {
        this.setAlias(alias);
        return this;
    }

    public <E extends Expression> E getExpression(Class<E> type) {
        return type.cast(getExpression());
    }

}
