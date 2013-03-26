package org.scriptella.mongodb.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class AbstractTestCase {
    protected void enableDebugLogging() {
        LoggingConfigurer.enableDebugLogging();
    }


    @BeforeClass
    @AfterClass
    public static void disableLogging() {
        LoggingConfigurer.disableDebugLogging();
    }
    //Empty for now
}
