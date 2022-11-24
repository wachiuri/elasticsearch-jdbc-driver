/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.querybuilders;

import com.threatseal.elasticsearch.jdbc.driver.expression.Branch;
import org.elasticsearch.index.query.QueryBuilder;

/**
 *
 * @author Timothy Wachiuri
 */
public interface EsQueryBuilder {

    public QueryBuilder toQueryBuilder();
}
