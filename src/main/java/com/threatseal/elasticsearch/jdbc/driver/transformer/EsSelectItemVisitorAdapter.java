package com.threatseal.elasticsearch.jdbc.driver.transformer;

import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;

import java.util.logging.Logger;

public class EsSelectItemVisitorAdapter extends SelectItemVisitorAdapter {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private String field;

    private String alias = null;

    @Override
    public void visit(SelectExpressionItem item) {

        String string = item.toString().trim();

        if (string.startsWith("`") && string.endsWith("`")) {
            string = string.substring(1, string.length() - 1);
        }

        String[] aliasExpression = string.split("\\b[aA][sS]\\b|\\s");
        if (aliasExpression.length > 1) {
            this.field = aliasExpression[0];
            this.alias = aliasExpression[1];
        } else {
            this.field = string;
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
