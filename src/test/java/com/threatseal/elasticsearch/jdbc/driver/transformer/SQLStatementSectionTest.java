/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threatseal.elasticsearch.jdbc.driver.transformer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Timothy Wachiuri
 */
public class SQLStatementSectionTest {

    public SQLStatementSectionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of values method, of class SQLStatementSection.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        SQLStatementSection[] expResult = {
            SQLStatementSection.WHERE,
            SQLStatementSection.GROUPBY,
            SQLStatementSection.ORDER,
            SQLStatementSection.LIMIT,};
        SQLStatementSection[] result = SQLStatementSection.values();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of valueOf method, of class SQLStatementSection.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String string = "WHERE";
        SQLStatementSection expResult = SQLStatementSection.WHERE;
        SQLStatementSection result = SQLStatementSection.valueOf(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}
