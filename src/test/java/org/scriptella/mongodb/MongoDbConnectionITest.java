package org.scriptella.mongodb;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scriptella.execution.EtlExecutor;
import scriptella.execution.EtlExecutorException;

import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class MongoDbConnectionITest {
    private static Logger rootLogger;
    private static ConsoleHandler h;

    @BeforeClass
    public static void enableDebugLogging() {
        rootLogger = Logger.getLogger("org.scriptella.mongodb");
        rootLogger.setLevel(Level.FINE);
        h = new ConsoleHandler();

        h.setFormatter(new SimpleFormatter());
        h.setLevel(Level.FINE);
        rootLogger.addHandler(h);
    }

    @AfterClass
    public static void disableDebugLogging() {
        if (rootLogger != null && h != null) {
            rootLogger.setLevel(null);
            rootLogger.removeHandler(h);
        }
    }


    @Test
    public void test() throws EtlExecutorException {

        String name = getClass().getSimpleName() + ".xml";
        System.out.println("name = " + name);
        URL resource = getClass().getResource(name);
        System.out.println("resource = " + resource);
        EtlExecutor ex = EtlExecutor.newExecutor(resource);
        ex.execute();

    }
}
