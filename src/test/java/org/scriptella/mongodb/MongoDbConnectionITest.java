package org.scriptella.mongodb;

import junit.framework.Assert;
import org.junit.Test;
import org.scriptella.mongodb.test.AbstractTestCase;
import scriptella.execution.EtlExecutor;
import scriptella.execution.EtlExecutorException;

import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class MongoDbConnectionITest extends AbstractTestCase {
    private static Set<String> messages = new TreeSet<String>();

    @Test
    public void test() throws EtlExecutorException {
        enableDebugLogging();

        String name = getClass().getSimpleName() + ".xml";
        URL resource = getClass().getResource(name);
        EtlExecutor ex = EtlExecutor.newExecutor(resource);
        messages.clear();
        ex.execute();
        //Verify that messages have arrived though the {@link #callback}
        Assert.assertEquals(2, messages.size());
        Iterator<String> it = messages.iterator();
        Assert.assertEquals("v1,Hello world", it.next());
        Assert.assertEquals("v2,First post", it.next());
    }

    @SuppressWarnings("unused") //Called from JavaScript in xml file
    public static void callback(String msg) {
        messages.add(msg);
    }

}
