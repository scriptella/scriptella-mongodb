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
        String s = wrapIntoStatement("db.runCommand", null, "{testCommand:1, value:'2'}");
        StringResource r = new StringResource(s);
        JsonStatementsParser statement = se.compile(r);
        Object v = statement.getOperations().get(0).getFirstArgumentAsBson().get("testCommand");
        Assert.assertEquals("1", String.valueOf(v));
        Assert.assertNotNull(statement);
        JsonStatementsParser statement2 = se.compile(r);
        Assert.assertTrue("Statement should be reused from the cache", statement == statement2);
    }

    public static String wrapIntoStatement(String opName, String collectionName, String json) {
        String s = "{\n operation: '" + opName + "', \n";
        if (collectionName != null) {
            s += " collection: ''" + collectionName + "', ";
        }
        s += " data: " + json + "\n}";
        return s;
    }
}
