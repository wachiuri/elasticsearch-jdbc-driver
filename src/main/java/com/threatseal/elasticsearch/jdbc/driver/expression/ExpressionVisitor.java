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

import com.threatseal.elasticsearch.jdbc.driver.expression.operators.arithmetic.*;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.conditional.EsAndExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.conditional.EsOrExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.conditional.EsXorExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SubSelect;

public interface ExpressionVisitor {

    void visit(EsBitwiseRightShift aThis);

    void visit(EsBitwiseLeftShift aThis);

    void visit(EsNullValue nullValue);

    void visit(Function function);

    void visit(EsSignedExpression signedExpression);

    void visit(JdbcParameter jdbcParameter);

    void visit(JdbcNamedParameter jdbcNamedParameter);

    void visit(EsDoubleValue doubleValue);

    void visit(EsLongValue longValue);

    void visit(HexValue hexValue);

    void visit(EsDateValue dateValue);

    void visit(EsTimeValue timeValue);

    void visit(EsTimestampValue timestampValue);

    void visit(EsParenthesis parenthesis);

    void visit(EsStringValue stringValue);

    void visit(EsAddition addition);

    void visit(EsDivision division);

    void visit(EsIntegerDivision division);

    void visit(EsMultiplication multiplication);

    void visit(EsSubtraction subtraction);

    void visit(EsAndExpression andExpression);

    void visit(EsOrExpression orExpression);

    void visit(EsXorExpression orExpression);

    void visit(Between between);

    void visit (OverlapsCondition overlapsCondition);

    void visit(EsEqualsTo equalsTo);

    void visit(GreaterThan greaterThan);

    void visit(GreaterThanEquals greaterThanEquals);

    void visit(InExpression inExpression);

    void visit(FullTextSearch fullTextSearch);

    void visit(IsNullExpression isNullExpression);

    void visit(IsBooleanExpression isBooleanExpression);

    void visit(LikeExpression likeExpression);

    void visit(MinorThan minorThan);

    void visit(MinorThanEquals minorThanEquals);

    void visit(NotEqualsTo notEqualsTo);

    void visit(Column tableColumn);

    void visit(SubSelect subSelect);

    void visit(CaseExpression caseExpression);

    void visit(WhenClause whenClause);

    void visit(ExistsExpression existsExpression);

    void visit(AnyComparisonExpression anyComparisonExpression);

    void visit(EsConcat concat);

    void visit(Matches matches);

    void visit(EsBitwiseAnd bitwiseAnd);

    void visit(EsBitwiseOr bitwiseOr);

    void visit(EsBitwiseXor bitwiseXor);

    void visit(CastExpression cast);

    void visit(TryCastExpression cast);

    void visit(EsModulo modulo);

    void visit(AnalyticExpression aexpr);

    void visit(ExtractExpression eexpr);

    void visit(IntervalExpression iexpr);

    void visit(OracleHierarchicalExpression oexpr);

    void visit(RegExpMatchOperator rexpr);

    void visit(JsonExpression jsonExpr);

    void visit(JsonOperator jsonExpr);

    void visit(RegExpMySQLOperator regExpMySQLOperator);

    void visit(UserVariable var);

    void visit(NumericBind bind);

    void visit(KeepExpression aexpr);

    void visit(MySQLGroupConcat groupConcat);

    void visit(ValueListExpression valueList);

    void visit(RowConstructor rowConstructor);

    void visit(RowGetExpression rowGetExpression);

    void visit(OracleHint hint);

    void visit(TimeKeyExpression timeKeyExpression);

    void visit(DateTimeLiteralExpression literal);

    void visit(NotExpression aThis);

    void visit(NextValExpression aThis);

    void visit(CollateExpression aThis);

    void visit(SimilarToExpression aThis);

    void visit(ArrayExpression aThis);

    void visit(ArrayConstructor aThis);

    void visit(VariableAssignment aThis);

    void visit(XMLSerializeExpr aThis);

    void visit(TimezoneExpression aThis);

    void visit(JsonAggregateFunction aThis);

    void visit(JsonFunction aThis);

    void visit(ConnectByRootOperator aThis);

    void visit(OracleNamedFunctionParameter aThis);

    void visit(AllColumns allColumns);

    void visit(AllTableColumns allTableColumns);

    void visit(AllValue allValue);

    void visit(IsDistinctExpression isDistinctExpression);

    void visit(GeometryDistance geometryDistance);
}
