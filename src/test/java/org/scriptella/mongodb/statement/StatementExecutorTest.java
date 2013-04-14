package org.scriptella.mongodb.statement;

import com.mongodb.DBObject;
import junit.framework.Assert;
import org.junit.Test;
import org.scriptella.mongodb.bridge.MongoMockBridge;
import scriptella.configuration.StringResource;
import scriptella.spi.ParametersCallback;
import scriptella.spi.support.MapParametersCallback;

import javax.sql.rowset.serial.SerialClob;
import java.sql.Clob;
import java.util.Collections;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class StatementExecutorTest {
    @Test
    public void testExecuteScriptWithParameterConversion() throws Exception {
        MongoMockBridge b = new MongoMockBridge();
        StatementExecutor se = new StatementExecutor(b);
        String s = wrapIntoStatement("db.runCommand", null, "{testCommand:1, value:'?clob'}");
        Clob clob = new SerialClob("testClob".toCharArray());
        ParametersCallback pc = new MapParametersCallback(Collections.singletonMap("clob", clob));
        se.executeScript(new StringResource(s), pc);
        Assert.assertEquals(1, b.getCommands().size());
        DBObject arg1 = (DBObject) b.getArgs().get(0);
        Assert.assertEquals("testClob", arg1.get("value"));
        se.close();
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
