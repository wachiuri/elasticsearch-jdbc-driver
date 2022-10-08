/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.transformer;

import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsDateValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsDoubleValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsLongValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsNullValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsParenthesis;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsSignedExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsStringValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsTimeValue;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsAddition;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.EsDivision;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsComparisonOperator;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsEqualsTo;
import com.threatseal.elasticsearch.jdbc.driver.querybuilders.EsBoolQueryBuilder;
import com.threatseal.elasticsearch.jdbc.driver.schema.EsColumn;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import org.elasticsearch.index.query.QueryBuilder;

/**
 *
 * @author Timothy Wachiuri
 */
public class EsExpressionVisitorAdapter extends ExpressionVisitorAdapter {

    private static final Logger logger = Logger.getLogger(EsExpressionVisitorAdapter.class.getName());

    private final SQLStatementSection sQLStatementSection;

    private final Stack<Branch> stack = new Stack();

    private final List<String> list = new ArrayList();

    public EsExpressionVisitorAdapter(SQLStatementSection sQLStatementSection) {

        this.sQLStatementSection = sQLStatementSection;

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

        System.out.println("message " + message);
        System.out.print("stack top value ");
        System.out.println(this.stack.size() > 0 ? this.stack.peek().toString() : "EMPTY");
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
        if (function.getParameters() != null) {
            function.getParameters().accept(this);
        }
        if (function.getKeep() != null) {
            function.getKeep().accept(this);
        }
        if (function.getOrderByElements() != null) {
            for (OrderByElement orderByElement : function.getOrderByElements()) {
                orderByElement.getExpression().accept(this);
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
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Multiplication expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Subtraction expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(AndExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        visitBinaryExpression(expr);
    }

    @Override
    public void visit(OrExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        visitBinaryExpression(expr);
    }

    @Override
    public void visit(XorExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Between expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        expr.getLeftExpression().accept(this);
        expr.getBetweenExpressionStart().accept(this);
        expr.getBetweenExpressionEnd().accept(this);
    }

    @Override
    public void visit(EqualsTo expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        Branch parent;

        if (this.stack.empty()) {
            System.out.println("stack is empty, adding EsBookQueryBuilder");
            parent = new EsBoolQueryBuilder(expr.getStringExpression());
        } else {
            System.out.println("stack is NOT empty, adding EsEqualsTo");
            parent = new EsEqualsTo();
        }

        this.stack.push(parent);

        visitBinaryExpression(expr);
    }

    @Override
    public void visit(GreaterThan expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(GreaterThanEquals expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(InExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        if (expr.getLeftExpression() != null) {
            expr.getLeftExpression().accept(this);
        }
        if (expr.getRightExpression() != null) {
            expr.getRightExpression().accept(this);
        } else if (expr.getRightItemsList() != null) {
            expr.getRightItemsList().accept(this);
        }
    }

    @Override
    public void visit(IsNullExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        expr.getLeftExpression().accept(this);
    }

    @Override
    public void visit(FullTextSearch expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        for (Column col : expr.getMatchColumns()) {
            col.accept(this);
        }
    }

    @Override
    public void visit(IsBooleanExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        expr.getLeftExpression().accept(this);
    }

    @Override
    public void visit(LikeExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(MinorThan expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(MinorThanEquals expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(NotEqualsTo expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
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
        if (expr.getSwitchExpression() != null) {
            expr.getSwitchExpression().accept(this);
        }
        for (Expression x : expr.getWhenClauses()) {
            x.accept(this);
        }
        if (expr.getElseExpression() != null) {
            expr.getElseExpression().accept(this);
        }
    }

    @Override
    public void visit(WhenClause expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        expr.getWhenExpression().accept(this);
        expr.getThenExpression().accept(this);
    }

    @Override
    public void visit(ExistsExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        expr.getRightExpression().accept(this);
    }

    @Override
    public void visit(AnyComparisonExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

    }

    @Override
    public void visit(Concat expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(Matches expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseAnd expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseOr expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseXor expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(CastExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        expr.getLeftExpression().accept(this);
    }

    @Override
    public void visit(TryCastExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        expr.getLeftExpression().accept(this);
    }

    @Override
    public void visit(Modulo expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(AnalyticExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        if (expr.getExpression() != null) {
            expr.getExpression().accept(this);
        }
        if (expr.getDefaultValue() != null) {
            expr.getDefaultValue().accept(this);
        }
        if (expr.getOffset() != null) {
            expr.getOffset().accept(this);
        }
        if (expr.getKeep() != null) {
            expr.getKeep().accept(this);
        }
        for (OrderByElement element : expr.getOrderByElements()) {
            element.getExpression().accept(this);
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
        expr.getExpression().accept(this);
    }

    @Override
    public void visit(IntervalExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
    }

    @Override
    public void visit(OracleHierarchicalExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        expr.getConnectExpression().accept(this);
        expr.getStartExpression().accept(this);
    }

    @Override
    public void visit(RegExpMatchOperator expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(ExpressionList expressionList) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        for (Expression expr : expressionList.getExpressions()) {
            expr.accept(this);
        }
    }

    @Override
    public void visit(NamedExpressionList namedExpressionList) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        for (Expression expr : namedExpressionList.getExpressions()) {
            expr.accept(this);
        }
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        for (ExpressionList list : multiExprList.getExprList()) {
            visit(list);
        }
    }

    @Override
    public void visit(NotExpression notExpr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        notExpr.getExpression().accept(this);
    }

    @Override
    public void visit(BitwiseRightShift expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(BitwiseLeftShift expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(JsonExpression jsonExpr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        jsonExpr.getExpression().accept(this);
    }

    @Override
    public void visit(JsonOperator expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(RegExpMySQLOperator expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(UserVariable var) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

    }

    @Override
    public void visit(NumericBind bind) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

    }

    @Override
    public void visit(KeepExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        for (OrderByElement element : expr.getOrderByElements()) {
            element.getExpression().accept(this);
        }
    }

    @Override
    public void visit(MySQLGroupConcat groupConcat) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        for (Expression expr : groupConcat.getExpressionList().getExpressions()) {
            expr.accept(this);
        }
        if (groupConcat.getOrderByElements() != null) {
            for (OrderByElement element : groupConcat.getOrderByElements()) {
                element.getExpression().accept(this);
            }
        }
    }

    @Override
    public void visit(ValueListExpression valueListExpression) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        for (Expression expr : valueListExpression.getExpressionList().getExpressions()) {
            expr.accept(this);
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
    }

    @Override
    public void visit(AllTableColumns allTableColumns) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
    }

    @Override
    public void visit(AllValue allValue) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
    }

    @Override
    public void visit(IsDistinctExpression isDistinctExpression) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(isDistinctExpression);
    }

    @Override
    public void visit(SelectExpressionItem selectExpressionItem) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        selectExpressionItem.getExpression().accept(this);
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
        rowGetExpression.getExpression().accept(this);
    }

    @Override
    public void visit(HexValue hexValue) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

    }

    @Override
    public void visit(OracleHint hint) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

    }

    @Override
    public void visit(TimeKeyExpression timeKeyExpression) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

    }

    @Override
    public void visit(DateTimeLiteralExpression literal) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
    }

    @Override
    public void visit(NextValExpression nextVal) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
    }

    @Override
    public void visit(CollateExpression col) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        col.getLeftExpression().accept(this);
    }

    @Override
    public void visit(SimilarToExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        visitBinaryExpression(expr);
    }

    @Override
    public void visit(ArrayExpression array) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        array.getObjExpression().accept(this);
        if (array.getIndexExpression() != null) {
            array.getIndexExpression().accept(this);
        }
        if (array.getStartIndexExpression() != null) {
            array.getStartIndexExpression().accept(this);
        }
        if (array.getStopIndexExpression() != null) {
            array.getStopIndexExpression().accept(this);
        }
    }

    @Override
    public void visit(ArrayConstructor aThis) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        for (Expression expression : aThis.getExpressions()) {
            expression.accept(this);
        }
    }

    @Override
    public void visit(VariableAssignment var) {
        log(new Object() {
        }.getClass().getEnclosingMethod());
        var.getVariable().accept(this);
        var.getExpression().accept(this);
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
        expr.getLeftExpression().accept(this);
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
        columnDefinition.accept(this);
    }

    @Override
    protected void visitBinaryExpression(BinaryExpression expr) {
        log(new Object() {
        }.getClass().getEnclosingMethod());

        EsComparisonOperator parent = (EsComparisonOperator) this.stack.peek();

        expr.getLeftExpression().accept(this);

        parent.setLeftExpression(this.stack.pop());

        expr.getRightExpression().accept(this);

        parent.setRightExpression(this.stack.pop());

    }
}
