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

import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

public class EsArrayExpression extends BranchImpl{

    private Branch objExpression;
    private Branch indexExpression;
    private Branch startIndexExpression;
    private Branch stopIndexExpression;


    public EsArrayExpression() {
        // empty constructor
    }

    public EsArrayExpression(Branch objExpression, Branch indexExpression, Branch startIndexExpression, Branch stopIndexExpression) {
        this.objExpression = objExpression;
        this.indexExpression = indexExpression;
        this.startIndexExpression = startIndexExpression;
        this.stopIndexExpression = stopIndexExpression;
    }

    public Branch getObjExpression() {
        return objExpression;
    }

    public void setObjExpression(Branch objExpression) {
        this.objExpression = objExpression;
    }

    public Branch getIndexExpression() {
        return indexExpression;
    }

    public void setIndexExpression(Branch indexExpression) {
        this.indexExpression = indexExpression;
    }

    public Branch getStartIndexExpression() {
        return startIndexExpression;
    }

    public void setStartIndexExpression(Branch startIndexExpression) {
        this.startIndexExpression = startIndexExpression;
    }

    public Branch getStopIndexExpression() {
        return stopIndexExpression;
    }

    public void setStopIndexExpression(Branch stopIndexExpression) {
        this.stopIndexExpression = stopIndexExpression;
    }

    @Override
    public String toString() {
        if (indexExpression != null) {
            return objExpression.toString() + "[" + indexExpression.toString() + "]";
        } else {
            return objExpression.toString() + "[" +
                    (startIndexExpression == null ? "" : startIndexExpression.toString()) +
                    ":" +
                    (stopIndexExpression == null ? "" : stopIndexExpression.toString()) +
                    "]";
        }
    }

    public EsArrayExpression withObjExpression(Branch objExpression) {
        this.setObjExpression(objExpression);
        return this;
    }

    public EsArrayExpression withIndexExpression(Branch indexExpression) {
        this.setIndexExpression(indexExpression);
        return this;
    }

    public EsArrayExpression withRangeExpression(Branch startIndexExpression, Branch stopIndexExpression) {
        this.setStartIndexExpression(startIndexExpression);
        this.setStopIndexExpression(stopIndexExpression);
        return this;
    }

    public <E extends Branch> E getObjExpression(Class<E> type) {
        return type.cast(getObjExpression());
    }

    public <E extends Branch> E getIndexExpression(Class<E> type) {
        return type.cast(getIndexExpression());
    }
}
