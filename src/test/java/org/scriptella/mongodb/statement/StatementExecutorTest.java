package org.scriptella.mongodb.statement;

import junit.framework.Assert;
import org.junit.Test;
import scriptella.configuration.StringResource;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class StatementExecutorTest {
    @Test
    public void testExecuteScript() throws Exception {

    }

    @Test
    public void testCompile() throws Exception {
        StatementExecutor se = new StatementExecutor();
        String s = "{testCommand:1, value:'2'}";
        StringResource r = new StringResource(s);
        BsonStatement statement = se.compile(r);
        Assert.assertNotNull(statement);
        BsonStatement statement2 = se.compile(r);
        Assert.assertTrue("Statement should be reused from the cache", statement==statement2);

    }
}
