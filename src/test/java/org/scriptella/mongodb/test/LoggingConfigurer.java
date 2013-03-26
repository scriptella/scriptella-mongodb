package org.scriptella.mongodb.test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class LoggingConfigurer {
    private static Logger rootLogger;
    private static ConsoleHandler h;

    public static synchronized void enableDebugLogging() {
        rootLogger = Logger.getLogger("org.scriptella.mongodb");
        rootLogger.setLevel(Level.FINE);
        h = new ConsoleHandler();

        h.setFormatter(new SimpleFormatter());
        h.setLevel(Level.FINE);
        rootLogger.addHandler(h);
    }

    public static synchronized void disableDebugLogging() {
        if (rootLogger != null && h != null) {
            rootLogger.setLevel(null);
            rootLogger.removeHandler(h);
        }
    }
}
