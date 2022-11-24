/*-
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2019 JSQLParser
 * %%
 * Dual licensed under GNU LGPL 2.1 or Apache License 2.0
 * #L%
 */
package com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import static com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsSupportsOldOracleJoinSyntax.NO_ORACLE_JOIN;
import com.threatseal.elasticsearch.jdbc.driver.querybuilders.EsQueryBuilder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import static net.sf.jsqlparser.expression.operators.relational.SupportsOldOracleJoinSyntax.ORACLE_JOIN_RIGHT;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class EsInExpression extends BranchImpl implements EsQueryBuilder {

    private Branch leftExpression;
    private EsItemsList rightItemsList;
    private boolean not = false;
    private Branch rightExpression;
    private int oldOracleJoinSyntax = NO_ORACLE_JOIN;

    public EsInExpression() {
    }

    public EsInExpression(Branch leftExpression, EsItemsList itemsList) {
        setLeftExpression(leftExpression);
        setRightItemsList(itemsList);
    }

    //@Override
    public void setOldOracleJoinSyntax(int oldOracleJoinSyntax) {
        this.oldOracleJoinSyntax = oldOracleJoinSyntax;
        if (oldOracleJoinSyntax < 0 || oldOracleJoinSyntax > 1) {
            throw new IllegalArgumentException(
                    "unexpected join type for oracle found with IN (type=" + oldOracleJoinSyntax + ")");
        }
    }

    //@Override
    public int getOldOracleJoinSyntax() {
        return oldOracleJoinSyntax;
    }

    public EsItemsList getRightItemsList() {
        return rightItemsList;
    }

    public Branch getLeftExpression() {
        return leftExpression;
    }

    public EsInExpression withRightItemsList(EsItemsList list) {
        this.setRightItemsList(list);
        return this;
    }

    public final void setRightItemsList(EsItemsList list) {
        rightItemsList = list;
    }

    public EsInExpression withLeftExpression(Branch expression) {
        this.setLeftExpression(expression);
        return this;
    }

    public final void setLeftExpression(Branch expression) {
        leftExpression = expression;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean b) {
        not = b;
    }

    public Branch getRightExpression() {
        return rightExpression;
    }

    public void setRightExpression(Branch rightExpression) {
        this.rightExpression = rightExpression;
    }

    private String getLeftExpressionString() {
        return leftExpression + (oldOracleJoinSyntax == ORACLE_JOIN_RIGHT ? "(+)" : "");
    }

    @Override
    public String toString() {
        StringBuilder statementBuilder = new StringBuilder();
        statementBuilder.append(getLeftExpressionString());

        statementBuilder.append(" ");
        if (not) {
            statementBuilder.append("NOT ");
        }
        statementBuilder.append("IN ");
        if (rightExpression == null) {
            statementBuilder.append(rightItemsList);
        } else {
            statementBuilder.append(rightExpression);
        }
        return statementBuilder.toString();
    }

    //@Override
    public int getOraclePriorPosition() {
        return EsSupportsOldOracleJoinSyntax.NO_ORACLE_PRIOR;
    }

    //@Override
    public void setOraclePriorPosition(int priorPosition) {
        if (priorPosition != EsSupportsOldOracleJoinSyntax.NO_ORACLE_PRIOR) {
            throw new IllegalArgumentException("unexpected prior for oracle found");
        }
    }

    public EsInExpression withRightExpression(Branch rightExpression) {
        this.setRightExpression(rightExpression);
        return this;
    }

    //@Override
    public EsInExpression withOldOracleJoinSyntax(int oldOracleJoinSyntax) {
        this.setOldOracleJoinSyntax(oldOracleJoinSyntax);
        return this;
    }

    //@Override
    public EsInExpression withOraclePriorPosition(int priorPosition) {
        this.setOraclePriorPosition(priorPosition);
        return this;
    }

    public EsInExpression withNot(boolean not) {
        this.setNot(not);
        return this;
    }

    public <E extends EsItemsList> E getRightItemsList(Class<E> type) {
        return type.cast(getRightItemsList());
    }

    public <E extends Branch> E getLeftExpression(Class<E> type) {
        return type.cast(getLeftExpression());
    }

    public <E extends Branch> E getRightExpression(Class<E> type) {
        return type.cast(getRightExpression());
    }

    @Override
    public QueryBuilder toQueryBuilder() {

        if (!isNot()) {
            if (getRightItemsList() != null) {
                return QueryBuilders.termsQuery(getLeftExpressionString(), getRightItemsList());
                
            } else {
                return QueryBuilders.termsQuery(getLeftExpressionString(), getRightExpression().toString());
            }
        } else {
            if (getRightItemsList() != null) {
                return QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery(getLeftExpressionString(), getRightItemsList()));
            } else {
                return QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery(getLeftExpressionString(), getRightExpression().toString()));
            }
        }
    }

}
