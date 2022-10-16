/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.querybuilders;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import java.util.UUID;
import net.sf.jsqlparser.expression.Expression;
import org.elasticsearch.index.query.QueryBuilders;

/**
 *
 * @author Timothy Wachiuri
 */
public class EsRangeQueryBuilder extends BranchImpl {

    private Branch leftExpression;
    private boolean not = false;
    private Branch betweenExpressionStart;
    private Branch betweenExpressionEnd;

    public EsRangeQueryBuilder() {
    }

    public EsRangeQueryBuilder(boolean not) {
        this.not = not;
    }

    public Branch getLeftExpression() {
        return leftExpression;
    }

    public void setLeftExpression(Branch leftExpression) {
        this.leftExpression = leftExpression;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    public Branch getBetweenExpressionStart() {
        return betweenExpressionStart;
    }

    public void setBetweenExpressionStart(Branch betweenExpressionStart) {
        this.betweenExpressionStart = betweenExpressionStart;
    }

    public Branch getBetweenExpressionEnd() {
        return betweenExpressionEnd;
    }

    public void setBetweenExpressionEnd(Branch betweenExpressionEnd) {
        this.betweenExpressionEnd = betweenExpressionEnd;
    }

    public EsRangeQueryBuilder withLeftExpression(Branch leftExpression) {
        this.leftExpression = leftExpression;
        return this;
    }

    public EsRangeQueryBuilder withBetweenExpressionStart(Branch betweenExpressionStart) {
        this.betweenExpressionStart = betweenExpressionStart;
        return this;
    }

    public EsRangeQueryBuilder withBetweenExpressionEnd(Branch betweenExpressionEnd) {
        this.betweenExpressionEnd = betweenExpressionEnd;
        return this;
    }

    @Override
    public Object toObject() {
        return QueryBuilders.rangeQuery(leftExpression.toString())
                .from(betweenExpressionStart, true)
                .to(betweenExpressionEnd, true);
    }

    @Override
    public String toString() {
        return leftExpression + " " + (not ? "NOT " : "") + "BETWEEN " + betweenExpressionStart + " AND "
                + betweenExpressionEnd;
    }

}
