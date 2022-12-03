/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.querybuilders;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import com.threatseal.elasticsearch.jdbc.driver.expression.BranchImpl;
import com.threatseal.elasticsearch.jdbc.driver.expression.EsBinaryExpression;
import com.threatseal.elasticsearch.jdbc.driver.expression.operators.relational.EsComparisonOperator;
import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 *
 * @author Timothy Wachiuri
 */
public class EsBoolQueryBuilder extends EsBinaryExpression implements EsCompoundQueryBuilder {

    List<EsQueryBuilder> mustExpressions = new ArrayList<>();

    public List<EsQueryBuilder> getMustExpressions() {
        return mustExpressions;
    }

    public void setMustExpressions(List<EsQueryBuilder> mustExpressions) {
        this.mustExpressions = mustExpressions;
    }
    
    public List<QueryBuilder> toQueryBuilders(){
        return new ArrayList<>();
    }

    @Override
    public BoolQueryBuilder toQueryBuilder() {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        for (EsQueryBuilder mustExpression : mustExpressions) {

        }

        return boolQueryBuilder;
    }

    @Override
    public String getStringExpression() {
        return "";
    }

}
