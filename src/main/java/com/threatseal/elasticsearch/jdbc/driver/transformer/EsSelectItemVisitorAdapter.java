package com.threatseal.elasticsearch.jdbc.driver.transformer;

import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;

public class EsSelectItemVisitorAdapter extends SelectItemVisitorAdapter {

    private String field;

    @Override
    public void visit(SelectExpressionItem item) {
        String[] expressions = item.toString().split("\\.");
        this.field = expressions[expressions.length - 1];
    }

    public String getField() {
        return field;
    }
}
