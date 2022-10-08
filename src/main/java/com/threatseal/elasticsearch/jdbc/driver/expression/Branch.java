/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.expression;

/**
 *
 * @author Timothy Wachiuri
 */
public interface Branch {

    public Branch getChild();

    public void setChild(Branch child);

    public Object toObject();

    @Override
    public String toString();
}
