/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.transformer;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsAllValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsAnalyticExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsAnyComparisonExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsArrayConstructor;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsArrayExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsCaseExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsCastExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsCollateExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsDateTimeLiteralExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsDateValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsDoubleValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsExtractExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsFunction;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsHexValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsIntervalExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsJdbcNamedParameter;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsJdbcParameter;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsJsonExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsKeepExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsLongValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsMySQLGroupConcat;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsNextValExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsNotExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsNullValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsNumericBind;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsOracleHierarchicalExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsOracleHint;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsParenthesis;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsRowConstructor;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsRowGetExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsSignedExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsStringValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsTimeKeyExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsTimeValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsTimezoneExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsTryCastExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsUserVariable;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsValueListExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsVariableAssignment;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsWhenClause;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsAddition;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsBitwiseAnd;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsBitwiseLeftShift;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsBitwiseOr;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsBitwiseRightShift;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsBitwiseXor;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsConcat;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsDivision;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsIntegerDivision;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsModulo;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsMultiplication;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsSubtraction;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.conditional.EsAndExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.conditional.EsOrExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.conditional.EsXorExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsBetween;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsEqualsTo;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsExistsExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsExpressionList;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsFullTextSearch;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsGreaterThan;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsGreaterThanEquals;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsInExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsIsBooleanExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsIsDistinctExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsIsNullExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsItemsList;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsJsonOperator;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsLikeExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsMatches;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsMinorThan;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsMinorThanEquals;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsMultiExpressionList;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsNamedExpressionList;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsNotEqualsTo;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsRegExpMatchOperator;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsRegExpMySQLOperator;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsSimilarToExpression;
import com.threatseal.elasticsearch.jdbc.driver.schema.EsColumn;
import com.threatseal.elasticsearch.jdbc.driver.statement.EsAllTableColumns;
import com.threatseal.elasticsearch.jdbc.driver.statement.EsOrderByElement;
import com.threatseal.elasticsearch.jdbc.driver.statement.create.table.EsColumnDefinition;
import com.threatseal.elasticsearch.jdbc.driver.statement.select.EsAllColumns;
import com.threatseal.elasticsearch.jdbc.driver.statement.select.EsSelectExpressionItem;
import net.sf.jsqlparser.expression.AllValue;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.ArrayConstructor;
import net.sf.jsqlparser.expression.ArrayExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.CollateExpression;
import net.sf.jsqlparser.expression.ConnectByRootOperator;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.JsonAggregateFunction;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.JsonFunction;
import net.sf.jsqlparser.expression.JsonFunctionExpression;
import net.sf.jsqlparser.expression.KeepExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.MySQLGroupConcat;
import net.sf.jsqlparser.expression.NextValExpression;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.NumericBind;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.OracleHint;
import net.sf.jsqlparser.expression.OracleNamedFunctionParameter;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.RowGetExpression;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeKeyExpression;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.TimezoneExpression;
import net.sf.jsqlparser.expression.TryCastExpression;
import net.sf.jsqlparser.expression.UserVariable;
import net.sf.jsqlparser.expression.ValueListExpression;
import net.sf.jsqlparser.expression.VariableAssignment;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.XMLSerializeExpr;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.IntegerDivision;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.conditional.XorExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.FullTextSearch;
import net.sf.jsqlparser.expression.operators.relational.GeometryDistance;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsBooleanExpression;
import net.sf.jsqlparser.expression.operators.relational.IsDistinctExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.JsonOperator;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.expression.operators.relational.SimilarToExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.ExpressionListItem;
import net.sf.jsqlparser.statement.select.FunctionItem;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.Pivot;
import net.sf.jsqlparser.statement.select.PivotXml;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.UnPivot;
import net.sf.jsqlparser.statement.select.WithItem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Timothy Wachiuri
 */
public class EsExpressionVisitorAdapter extends ExpressionVisitorAdapter {

    private final Logger logger = Logger.getLogger(EsExpressionVisitorAdapter.class.getName());

    private final SQLStatementSection sQLStatementSection;

    private final String timeZone;

    private final Stack<Branch> stack = new Stack();

    private final List<String> list = new ArrayList();

    public EsExpressionVisitorAdapter(SQLStatementSection sQLStatementSection, String timeZone) {

        this.sQLStatementSection = sQLStatementSection;
        this.timeZone = timeZone;

    }

    public Stack<Branch> getStack() {
        return stack;
    }

    public List<String> getList() {
        return list;
    }

    private void log(Method method) {
        String message = method.getDeclaringClass().getName()
                + method.getName() + " " + sQLStatementSection + " parameters ";
        for (Class<?> classs : method.getParameterTypes()) {
            message += classs.getSimpleName() + ",";
        }

        logger.log(Level.FINE, "message " + message);
        logger.log(Level.FINE, "stack [");
        stack.forEach(action -> {
            logger.log(Level.FINE, action + ":" + action.getClass().getSimpleName() + ",");
        });
        logger.log(Level.FINE, "]");
        logger.log(Level.FINE, "stack top " + (stack.empty() ? "EMPTY" : stack.peek()));
        //this.stack.push(method.getParameters()[0]);
        this.list.add("\"" + method.getParameters()[0].getType().getName() + "\"");
    }

    @Override
    public void visit(NullValue value) {

        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsNullValue());
    }

    @Override
    public void visit(Function function) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsFunction parent = new EsFunction()
                .withAllColumns(function.isAllColumns())
                .withAttributeName(function.getAttributeName())
                .withDistinct(function.isDistinct())
                .withIgnoreNulls(function.isIgnoreNulls())
                .withName(function.getName())
                .withUnique(function.isUnique());

        this.stack.push(parent);
        if (function.getParameters() != null) {
            function.getParameters().accept(this);
            parent.setParameters((EsExpressionList) this.stack.pop());
            ;
        }
        if (function.getNamedParameters() != null) {
            function.getNamedParameters().accept(this);
            parent.setNamedParameters((EsNamedExpressionList) this.stack.pop());
            ;
        }
        if (function.getKeep() != null) {
            function.getKeep().accept(this);
            parent.setKeep((EsKeepExpression) this.stack.pop());
        }
        if (function.getOrderByElements() != null) {
            for (OrderByElement orderByElement : function.getOrderByElements()) {
                orderByElement.getExpression().accept(this);
                parent.getOrderByElements().add((EsOrderByElement) this.stack.pop());
            }
        }
    }

    @Override
    public void visit(SignedExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsSignedExpression esSignedExpression = new EsSignedExpression(expr.getSign(), null);

        this.stack.push(esSignedExpression);
        expr.getExpression().accept(this);
        esSignedExpression.setChild(this.stack.pop());
    }

    @Override
    public void visit(JdbcParameter parameter) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
    }

    @Override
    public void visit(JdbcNamedParameter parameter) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

    }

    @Override
    public void visit(DoubleValue value) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsDoubleValue(value.toString()));
    }

    @Override
    public void visit(LongValue value) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsLongValue(value.getStringValue()));
    }

    @Override
    public void visit(DateValue value) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsDateValue(value.getValue()));
    }

    @Override
    public void visit(TimeValue value) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsTimeValue(value.getValue().toString()));
    }

    @Override
    public void visit(TimestampValue value) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        this.stack.push(new EsTimeValue(value.getValue().toString()));
    }

    @Override
    public void visit(Parenthesis parenthesis) {

        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsParenthesis parent = new EsParenthesis();
        this.stack.push(parent);
        parenthesis.getExpression().accept(this);
        parent.setChild(this.stack.pop());

    }

    @Override
    public void visit(StringValue value) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        this.stack.push(new EsStringValue(value.getValue()));
    }

    @Override
    public void visit(Addition expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsAddition());
        visitBinaryExpression(expr);

    }

    @Override
    public void visit(Division expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsDivision());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(IntegerDivision expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsIntegerDivision());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Multiplication expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsMultiplication());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Subtraction expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsSubtraction());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(AndExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        Branch parent = new EsAndExpression();
        this.stack.push(parent);

        visitBinaryExpression(expr);

    }

    @Override
    public void visit(OrExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsOrExpression());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(XorExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsXorExpression());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Between expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsBetween parent = new EsBetween().withNot(expr.isNot());

        this.stack.push(parent);

        expr.getLeftExpression().accept(this);
        parent.setLeftExpression(this.stack.pop());

        expr.getBetweenExpressionStart().accept(this);
        parent.setBetweenExpressionStart(this.stack.pop());

        expr.getBetweenExpressionEnd().accept(this);
        parent.setBetweenExpressionEnd(this.stack.pop());

        parent.setTimeZone(timeZone);

    }

    @Override
    public void visit(EqualsTo expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        Branch parent = new EsEqualsTo();

        this.stack.push(parent);

        visitBinaryExpression(expr);

    }

    @Override
    public void visit(GreaterThan expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsGreaterThan().withTimeZone(timeZone));
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(GreaterThanEquals expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsGreaterThanEquals().withTimeZone(timeZone));
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(InExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsInExpression parent = new EsInExpression();
        this.stack.push(new EsInExpression());

        if (expr.getLeftExpression() != null) {
            expr.getLeftExpression().accept(this);
            parent.setLeftExpression(this.stack.pop());

        }
        if (expr.getRightExpression() != null) {
            expr.getRightExpression().accept(this);
            parent.setRightExpression(this.stack.pop());
        } else if (expr.getRightItemsList() != null) {
            expr.getRightItemsList().accept(this);
            parent.setRightItemsList((EsItemsList) this.stack.pop());
        }
    }

    @Override
    public void visit(IsNullExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsIsNullExpression parent = new EsIsNullExpression();
        this.stack.push(parent);
        expr.getLeftExpression().accept(this);
        parent.setLeftExpression(this.stack.pop());
    }

    @Override
    public void visit(FullTextSearch expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsFullTextSearch parent = new EsFullTextSearch()
                .withSearchModifier(expr.getSearchModifier());
        this.stack.push(parent);

        expr.getAgainstValue().accept(this);

        Branch againstValue = this.stack.pop();

        if (againstValue instanceof EsStringValue) {
            parent.setAgainstValue((EsStringValue) this.stack.pop());
        } else if (againstValue instanceof EsJdbcParameter) {
            parent.setAgainstValue((EsJdbcParameter) this.stack.pop());
        } else if (againstValue instanceof EsJdbcNamedParameter) {
            parent.setAgainstValue((EsJdbcNamedParameter) this.stack.pop());
        }

        for (Column col : expr.getMatchColumns()) {
            col.accept(this);
            EsColumn column = (EsColumn) this.stack.pop();
            parent.getMatchColumns().add(column);
        }
    }

    @Override
    public void visit(IsBooleanExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsIsBooleanExpression parent = new EsIsBooleanExpression()
                .withIsTrue(expr.isTrue())
                .withNot(expr.isNot());

        this.stack.push(parent);
        expr.getLeftExpression().accept(this);

        parent.setLeftExpression(this.stack.pop());
    }

    @Override
    public void visit(LikeExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsLikeExpression parent = new EsLikeExpression()
                .withCaseInsensitive(expr.isCaseInsensitive())
                .withNot(expr.isNot());
        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(MinorThan expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsMinorThan parent = new EsMinorThan().withTimeZone(timeZone);
        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(MinorThanEquals expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsMinorThanEquals parent = new EsMinorThanEquals(expr.getStringExpression()).withTimeZone(timeZone);

        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(NotEqualsTo expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsNotEqualsTo parent = new EsNotEqualsTo(expr.getStringExpression());

        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Column column) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsColumn(column.getColumnName()));
    }

    @Override
    public void visit(SubSelect subSelect) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        if (this.getSelectVisitor() != null) {
            if (subSelect.getWithItemsList() != null) {
                for (WithItem item : subSelect.getWithItemsList()) {
                    item.accept(this.getSelectVisitor());
                }
            }
            subSelect.getSelectBody().accept(this.getSelectVisitor());
        }
        if (subSelect.getPivot() != null) {
            subSelect.getPivot().accept(this);
        }
    }

    @Override
    public void visit(CaseExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsCaseExpression parent = new EsCaseExpression()
                .withUsingBrackets(expr.isUsingBrackets());

        this.stack.push(parent);

        if (expr.getSwitchExpression() != null) {
            expr.getSwitchExpression().accept(this);
            parent.setSwitchExpression(this.stack.pop());
        }
        for (Expression x : expr.getWhenClauses()) {
            x.accept(this);
            parent.getWhenClauses().add((EsWhenClause) this.stack.pop());
        }
        if (expr.getElseExpression() != null) {
            expr.getElseExpression().accept(this);
            parent.setElseExpression(this.stack.pop());
        }
    }

    @Override
    public void visit(WhenClause expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsWhenClause parent = new EsWhenClause();
        this.stack.push(parent);

        expr.getWhenExpression().accept(this);

        parent.setWhenExpression(this.stack.pop());

        expr.getThenExpression().accept(this);

        parent.setThenExpression(this.stack.pop());
    }

    @Override
    public void visit(ExistsExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsExistsExpression parent = new EsExistsExpression().withNot(expr.isNot());

        this.stack.push(parent);
        expr.getRightExpression().accept(this);

        parent.setRightExpression(this.stack.pop());
    }

    @Override
    public void visit(AnyComparisonExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsAnyComparisonExpression parent;

        if (expr.isUsingSubSelect()) {
            parent = new EsAnyComparisonExpression(expr.getAnyType(), expr.getSubSelect())
                    .withUseBracketsForValues(expr.isUsingBracketsForValues());

        } else {
            expr.getItemsList().accept(this);
            parent = new EsAnyComparisonExpression(expr.getAnyType(), (EsItemsList) this.stack.pop());
        }

        this.stack.push(parent);
    }

    @Override
    public void visit(Concat expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsConcat parent = new EsConcat();
        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Matches expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsMatches parent = new EsMatches()
                .withOldOracleJoinSyntax(expr.getOldOracleJoinSyntax())
                .withOraclePriorPosition(expr.getOraclePriorPosition());
        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseAnd expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsBitwiseAnd parent = new EsBitwiseAnd();

        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseOr expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsBitwiseOr parent = new EsBitwiseOr();
        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseXor expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsBitwiseXor parent = new EsBitwiseXor();
        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(CastExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsCastExpression parent = new EsCastExpression()
                .withType(expr.getType())
                .withUseCastKeyword(expr.isUseCastKeyword());
        this.stack.push(parent);

        expr.getRowConstructor().accept(this);

        parent.setRowConstructor((EsRowConstructor) this.stack.pop());

        expr.getLeftExpression().accept(this);

        parent.setLeftExpression(this.stack.pop());
    }

    @Override
    public void visit(TryCastExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsTryCastExpression parent = new EsTryCastExpression()
                .withType(expr.getType())
                .withUseCastKeyword(expr.isUseCastKeyword());

        this.stack.push(parent);

        expr.getRowConstructor().accept(this);

        parent.setRowConstructor((EsRowConstructor) this.stack.pop());

        expr.getLeftExpression().accept(this);

        parent.setLeftExpression(this.stack.pop());
    }

    @Override
    public void visit(Modulo expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsModulo parent = new EsModulo();
        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(AnalyticExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsAnalyticExpression parent = new EsAnalyticExpression()
                .withAllColumns(expr.isAllColumns())
                .withDistinct(expr.isDistinct())
                .withIgnoreNulls(expr.isIgnoreNulls())
                .withName(expr.getName())
                .withType(expr.getType())
                .withUnique(expr.isUnique())
                .withWindowElement(expr.getWindowElement());

        this.stack.push(parent);

        if (expr.getExpression() != null) {
            expr.getExpression().accept(this);
            parent.setExpression(this.stack.pop());
        }
        if (expr.getDefaultValue() != null) {
            expr.getDefaultValue().accept(this);
            parent.setDefaultValue(this.stack.pop());
        }
        if (expr.getOffset() != null) {
            expr.getOffset().accept(this);
            parent.setOffset(this.stack.pop());
        }
        if (expr.getKeep() != null) {
            expr.getKeep().accept(this);
            parent.setKeep((EsKeepExpression) this.stack.pop());
        }
        for (OrderByElement element : expr.getOrderByElements()) {
            element.getExpression().accept(this);
            parent.getOrderByElements().add((OrderByElement) this.stack.pop());
        }

        if (expr.getWindowElement() != null) {
            expr.getWindowElement().getRange().getStart().getExpression().accept(this);
            expr.getWindowElement().getRange().getEnd().getExpression().accept(this);
            expr.getWindowElement().getOffset().getExpression().accept(this);
        }
    }

    @Override
    public void visit(ExtractExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsExtractExpression parent = new EsExtractExpression()
                .withName(expr.getName());

        this.stack.push(parent);
        expr.getExpression().accept(this);

        parent.setExpression(this.stack.pop());
    }

    @Override
    public void visit(IntervalExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsIntervalExpression parent = new EsIntervalExpression()
                .withIntervalType(expr.getIntervalType())
                .withParameter(expr.getParameter());
        this.stack.push(parent);
        expr.getExpression().accept(this);
        parent.setExpression(this.stack.pop());
    }

    @Override
    public void visit(OracleHierarchicalExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsOracleHierarchicalExpression parent = new EsOracleHierarchicalExpression()
                .withConnectFirst(expr.isConnectFirst())
                .withNoCycle(expr.isNoCycle());

        this.stack.push(parent);
        expr.getConnectExpression().accept(this);
        parent.setConnectExpression(this.stack.pop());

        expr.getStartExpression().accept(this);
        parent.setStartExpression(this.stack.pop());
    }

    @Override
    public void visit(RegExpMatchOperator expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsRegExpMatchOperator parent = new EsRegExpMatchOperator(expr.getOperatorType());

        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(ExpressionList expressionList) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsExpressionList parent = new EsExpressionList()
                .withUsingBrackets(expressionList.isUsingBrackets());

        this.stack.push(parent);

        for (Expression expr : expressionList.getExpressions()) {
            expr.accept(this);
            parent.getExpressions().add(this.stack.pop());
        }
    }

    @Override
    public void visit(NamedExpressionList namedExpressionList) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsNamedExpressionList parent = new EsNamedExpressionList()
                .withNames(namedExpressionList.getNames());
        this.stack.push(parent);
        for (Expression expr : namedExpressionList.getExpressions()) {
            expr.accept(this);
            parent.getExpressions().add(this.stack.pop());
        }
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsMultiExpressionList parent = new EsMultiExpressionList();

        this.stack.push(parent);

        for (ExpressionList list : multiExprList.getExprList()) {
            visit(list);
            parent.getExpressionLists().add((EsExpressionList) this.stack.pop());
        }
    }

    @Override
    public void visit(NotExpression notExpr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsNotExpression parent = new EsNotExpression()
                .withExclamationMark(notExpr.isExclamationMark());

        this.stack.push(parent);
        notExpr.getExpression().accept(this);

        parent.setExpression(this.stack.pop());
    }

    @Override
    public void visit(BitwiseRightShift expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsBitwiseRightShift parent = new EsBitwiseRightShift();

        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseLeftShift expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsBitwiseLeftShift parent = new EsBitwiseLeftShift();

        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(JsonExpression jsonExpr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsJsonExpression parent = new EsJsonExpression();

        this.stack.push(parent);
        jsonExpr.getExpression().accept(this);

        parent.setExpression(this.stack.pop());
    }

    @Override
    public void visit(JsonOperator expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsJsonOperator parent = new EsJsonOperator(expr.getStringExpression());
        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(RegExpMySQLOperator expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsRegExpMySQLOperator parent = new EsRegExpMySQLOperator(
                expr.isNot(),
                expr.getOperatorType()
        );

        if (expr.isUseRLike()) {
            parent.useRLike();
        }

        this.stack.push(parent);
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(UserVariable var) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsUserVariable()
                .withDoubleAdd(var.isDoubleAdd())
                .withName(var.getName())
        );
    }

    @Override
    public void visit(NumericBind bind) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsNumericBind()
                .withBindId(bind.getBindId())
        );
    }

    @Override
    public void visit(KeepExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsKeepExpression parent = new EsKeepExpression()
                .withFirst(expr.isFirst())
                .withName(expr.getName());
        this.stack.push(parent);
        for (OrderByElement element : expr.getOrderByElements()) {
            element.getExpression().accept(this);
            parent.getOrderByElements().add((EsOrderByElement) this.stack.pop());
        }
    }

    @Override
    public void visit(MySQLGroupConcat groupConcat) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsMySQLGroupConcat parent = new EsMySQLGroupConcat()
                .withDistinct(groupConcat.isDistinct())
                .withSeparator(groupConcat.getSeparator());

        this.stack.push(parent);
        for (Expression expr : groupConcat.getExpressionList().getExpressions()) {
            expr.accept(this);
            parent.getExpressionList().addExpressions(this.stack.pop());
        }
        if (groupConcat.getOrderByElements() != null) {
            for (OrderByElement element : groupConcat.getOrderByElements()) {
                element.getExpression().accept(this);
                parent.addOrderByElements((EsOrderByElement) this.stack.pop());
            }
        }
    }

    @Override
    public void visit(ValueListExpression valueListExpression) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsValueListExpression parent = new EsValueListExpression();

        this.stack.push(parent);

        for (Expression expr : valueListExpression.getExpressionList().getExpressions()) {
            expr.accept(this);
            parent.getExpressionList().addExpressions(this.stack.pop());
        }
    }

    @Override
    public void visit(Pivot pivot) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        for (FunctionItem item : pivot.getFunctionItems()) {
            item.getFunction().accept(this);
        }
        for (Column col : pivot.getForColumns()) {
            col.accept(this);
        }
        if (pivot.getSingleInItems() != null) {
            for (SelectExpressionItem item : pivot.getSingleInItems()) {
                item.accept(this);
            }
        }

        if (pivot.getMultiInItems() != null) {
            for (ExpressionListItem item : pivot.getMultiInItems()) {
                item.getExpressionList().accept(this);
            }
        }
    }

    @Override
    public void visit(PivotXml pivot) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        for (FunctionItem item : pivot.getFunctionItems()) {
            item.getFunction().accept(this);
        }
        for (Column col : pivot.getForColumns()) {
            col.accept(this);
        }
        if (pivot.getInSelect() != null && this.getSelectVisitor() != null) {
            pivot.getInSelect().accept(this.getSelectVisitor());
        }
    }

    @Override
    public void visit(UnPivot unpivot) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        unpivot.accept(this);
    }

    @Override
    public void visit(AllColumns allColumns) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsAllColumns());
    }

    @Override
    public void visit(AllTableColumns allTableColumns) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsAllTableColumns().withTable(allTableColumns.getTable()));
    }

    @Override
    public void visit(AllValue allValue) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsAllValue());
    }

    @Override
    public void visit(IsDistinctExpression isDistinctExpression) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsIsDistinctExpression parent = new EsIsDistinctExpression();
        parent.setNot(isDistinctExpression.isNot());

        this.stack.push(parent);

        visitBinaryExpression(isDistinctExpression);
    }

    @Override
    public void visit(SelectExpressionItem selectExpressionItem) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsSelectExpressionItem parent = new EsSelectExpressionItem()
                .withAlias(selectExpressionItem.getAlias());

        this.stack.push(parent);

        selectExpressionItem.getExpression().accept(this);

        parent.setExpression(this.stack.pop());
    }

    @Override
    public void visit(RowConstructor rowConstructor) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        if (rowConstructor.getColumnDefinitions().isEmpty()) {
            for (Expression expression : rowConstructor.getExprList().getExpressions()) {
                expression.accept(this);
            }
        } else {
            for (ColumnDefinition columnDefinition : rowConstructor.getColumnDefinitions()) {
                columnDefinition.accept(this);
            }
        }
    }

    @Override
    public void visit(RowGetExpression rowGetExpression) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsRowGetExpression parent = new EsRowGetExpression(rowGetExpression.getColumnName());

        this.stack.push(parent);

        rowGetExpression.getExpression().accept(this);

        parent.setExpression(this.stack.pop());

    }

    @Override
    public void visit(HexValue hexValue) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsHexValue(hexValue.getValue()));
    }

    @Override
    public void visit(OracleHint hint) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsOracleHint()
                .withSingleLine(hint.isSingleLine())
                .withValue(hint.getValue())
        );
    }

    @Override
    public void visit(TimeKeyExpression timeKeyExpression) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsTimeKeyExpression(timeKeyExpression.getStringValue()));

    }

    @Override
    public void visit(DateTimeLiteralExpression literal) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsDateTimeLiteralExpression()
                .withType(literal.getType())
                .withValue(literal.getValue())
        );
    }

    @Override
    public void visit(NextValExpression nextVal) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsNextValExpression(nextVal.getNameList())
                .withNextValueFor(nextVal.isUsingNextValueFor())
        );
    }

    @Override
    public void visit(CollateExpression col) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsCollateExpression parent = new EsCollateExpression().withCollate(col.getCollate());
        this.stack.push(parent);
        col.getLeftExpression().accept(this);

        parent.setLeftExpression(this.stack.pop());
    }

    @Override
    public void visit(SimilarToExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsSimilarToExpression parent = new EsSimilarToExpression()
                .withEscape(expr.getEscape())
                .withNot(expr.isNot());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(ArrayExpression array) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsArrayExpression parent = new EsArrayExpression();

        this.stack.push(parent);

        array.getObjExpression().accept(this);

        parent.setObjExpression(this.stack.pop());

        if (array.getIndexExpression() != null) {
            array.getIndexExpression().accept(this);

            parent.setIndexExpression(this.stack.pop());
        }
        if (array.getStartIndexExpression() != null) {
            array.getStartIndexExpression().accept(this);
            parent.setStartIndexExpression(this.stack.pop());
        }
        if (array.getStopIndexExpression() != null) {
            array.getStopIndexExpression().accept(this);
            parent.setStopIndexExpression(this.stack.pop());
        }
    }

    @Override
    public void visit(ArrayConstructor aThis) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsArrayConstructor parent = new EsArrayConstructor(true);

        this.stack.push(parent);
        for (Expression expression : aThis.getExpressions()) {
            expression.accept(this);
            parent.getExpressions().add(this.stack.pop());
        }
    }

    @Override
    public void visit(VariableAssignment var) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsVariableAssignment parent = new EsVariableAssignment();
        parent.setOperation(var.getOperation());

        this.stack.push(parent);

        var.getVariable().accept(this);

        parent.setVariable((EsUserVariable) this.stack.pop());

        var.getExpression().accept(this);

        parent.setExpression(this.stack.pop());
    }

    @Override
    public void visit(XMLSerializeExpr expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        expr.getExpression().accept(this);
        for (OrderByElement elm : expr.getOrderByElements()) {
            elm.getExpression().accept(this);
        }
    }

    @Override
    public void visit(TimezoneExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsTimezoneExpression parent = new EsTimezoneExpression();

        this.stack.push(parent);

        expr.getLeftExpression().accept(this);
        parent.setLeftExpression(this.stack.pop());
    }

    @Override
    public void visit(JsonAggregateFunction expression) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        Expression expr = expression.getExpression();
        if (expr != null) {
            expr.accept(this);
        }

        expr = expression.getFilterExpression();
        if (expr != null) {
            expr.accept(this);
        }
    }

    @Override
    public void visit(JsonFunction expression) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        for (JsonFunctionExpression expr : expression.getExpressions()) {
            expr.getExpression().accept(this);
        }
    }

    @Override
    public void visit(ConnectByRootOperator connectByRootOperator) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        connectByRootOperator.getColumn().accept(this);
    }

    @Override
    public void visit(OracleNamedFunctionParameter oracleNamedFunctionParameter) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        oracleNamedFunctionParameter.getExpression().accept(this);
    }

    @Override
    public void visit(GeometryDistance geometryDistance) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(geometryDistance);
    }

    @Override
    public void visit(ColumnDefinition columnDefinition) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        this.stack.push(new EsColumnDefinition(columnDefinition.getColumnName(),
                columnDefinition.getColDataType(), columnDefinition.getColumnSpecs()));

        //columnDefinition.accept(this);
    }

    @Override
    protected void visitBinaryExpression(BinaryExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        logger.log(Level.FINE, "BinaryExpression LEFT ");
        logger.log(Level.FINE, expr.getLeftExpression().toString());
        logger.log(Level.FINE, " " + expr.getStringExpression() + " ");
        logger.log(Level.FINE, " RIGHT ");
        logger.log(Level.FINE, expr.getRightExpression().toString());

        EsBinaryExpression parent = (EsBinaryExpression) this.stack.peek();

        expr.getLeftExpression().accept(this);

        parent.setLeftExpression(this.stack.pop());

        expr.getRightExpression().accept(this);

        parent.setRightExpression(this.stack.pop());

    }
}
