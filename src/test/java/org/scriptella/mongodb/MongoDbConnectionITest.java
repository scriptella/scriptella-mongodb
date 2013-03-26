package org.scriptella.mongodb;

import org.junit.Test;
import org.scriptella.mongodb.test.AbstractTestCase;
import scriptella.execution.EtlExecutor;
import scriptella.execution.EtlExecutorException;

import java.net.URL;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class MongoDbConnectionITest extends AbstractTestCase {

    @Test
    public void test() throws EtlExecutorException {
        enableDebugLogging();

        String name = getClass().getSimpleName() + ".xml";
        System.out.println("name = " + name);
        URL resource = getClass().getResource(name);
        System.out.println("resource = " + resource);
        EtlExecutor ex = EtlExecutor.newExecutor(resource);
        ex.execute();

    }
}
