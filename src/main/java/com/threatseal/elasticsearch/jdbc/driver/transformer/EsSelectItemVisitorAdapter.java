package com.threatseal.elasticsearch.jdbc.driver.transformer;

import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;

public class EsSelectItemVisitorAdapter extends SelectItemVisitorAdapter {

    private String field;

    private String alias = null;

    @Override
    public void visit(SelectExpressionItem item) {
        String[] aliasExpression = item.toString().split("\\b[aA][sS]\\b|\\s");
        if (aliasExpression.length > 1) {
            this.field = aliasExpression[0];
            this.alias = aliasExpression[1];
        } else {
            this.field = item.toString();
        }

        String[] expressions = this.field.split("\\.");
        this.field = expressions[expressions.length - 1];
    }

    public String getField() {
        return field;
    }

    public String getAlias() {
        return alias;
    }
}
