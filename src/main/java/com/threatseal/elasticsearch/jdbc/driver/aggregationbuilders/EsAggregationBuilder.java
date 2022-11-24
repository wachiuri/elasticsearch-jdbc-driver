/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.aggregationbuilders;

import org.elasticsearch.search.aggregations.AggregationBuilder;

/**
 *
 * @author Timothy Wachiuri
 */
public interface EsAggregationBuilder {

    public AggregationBuilder toAggregationBuilder();
}
