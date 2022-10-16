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
public class BranchImpl implements Branch {

    private Branch child;

    @Override
    public Branch getChild() {
        return child;
    }

    @Override
    public void setChild(Branch child) {
        this.child = child;
    }

    @Override
    public Object toObject() {
        return this;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
