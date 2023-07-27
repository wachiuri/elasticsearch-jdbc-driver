package com.threatseal.elasticsearch.jdbc.driver.transformer;

import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EsSelectItemVisitorAdapter extends SelectItemVisitorAdapter {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private String field;

    private String alias = null;

    @Override
    public void visit(SelectExpressionItem item) {

        logger.log(Level.FINE, "item expression {0}", item.getExpression().toString());

        String string = item.toString().trim();

        logger.log(Level.FINE, "trimmed {0}", string);

        logger.log(Level.FINE, "starts with ` {0}", string.startsWith("`"));
        logger.log(Level.FINE, "ends with ` {0}", string.endsWith("`"));

        if (string.startsWith("`") || string.endsWith("`")) {
            string = string.substring(1, string.length() - 2);
        }

        logger.log(Level.FINE, "unquoted {0}", string);

        String[] aliasExpression = string.split("\\b[aA][sS]\\b|\\s");

        logger.log(Level.FINE, "aliasExpression {0}", new Object[]{aliasExpression[0], aliasExpression[1]});

        if (aliasExpression.length > 1) {
            this.field = aliasExpression[0];
            this.alias = aliasExpression[1];
        } else {
            this.field = string;
        }

        logger.log(Level.FINE, "field {0} alias {1}", new String[]{this.field, this.alias});
    }

    public String getField() {
        return field;
    }

    public String getAlias() {
        return alias;
    }
}
