package org.scriptella.mongodb.statement;

import org.junit.Assert;
import org.junit.Test;
import scriptella.spi.support.MapParametersCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fyodor Kupolov <scriptella@gmail.com>
 */
public class BsonStatementTest {
    @Test
    public void testParse()
            throws Exception {
        String json = StatementExecutorTest.wrapIntoStatement("db.runCommand", null, "{\n" + "  '_id': '10280',\n" + "  'city': '?{city}',\n" + "  \"state\": \"?state\",\n" +
                "  \"pop\": 5574,\n" + "  \"loc\": [\n" + "    '?loc1',\n" + "    40.710537\n" +
                "  ]\n" + "}");
        BsonStatement s =
                new BsonStatement(json);
        final List<BsonStatement.ObjectBindings> bindings = s.getBindings();
        Assert.assertEquals(2,
                bindings.size());

        Map<String, BsonStatement.ObjectBinding> pMap = new HashMap<String, BsonStatement.ObjectBinding>();
        BsonStatement.ObjectBindings b1 = bindings.get(0);
        BsonStatement.ObjectBindings b2 = bindings.get(1);
        List<BsonStatement.ObjectBinding> list = new ArrayList<BsonStatement.ObjectBinding>(b1.bindingsList);
        list.addAll(b2.bindingsList);

        for (BsonStatement.ObjectBinding objectBinding : list) {
            pMap.put(objectBinding.property, objectBinding);
        }

        //Check each of 3 parameters
        Assert.assertEquals(3,
                list.size());
        Assert.assertEquals("loc1", pMap.get("0").variableName);
        Assert.assertEquals("state", pMap.get("state").variableName);
        Assert.assertEquals("city",
                pMap.get("city").expression.getExpression());
    }

    @Test
    public void testSetParamsParse()
            throws Exception {
        String json = StatementExecutorTest.wrapIntoStatement("db.runCommand", null, "{\n" + "  '_id': '10280',\n" + "  'city': '?{city}',\n" + "  \"state\": \"?state\",\n" +
                "  \"pop\": 5574,\n" + "  \"loc\": [\n" + "    '?loc1',\n" + "    37.7753\n" + "  ]\n" +
                "}");
        BsonStatement s =
                new BsonStatement(json);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("city", "San Francisco");
        map.put("state", "CA");
        map.put("loc1", -122.42);

        MapParametersCallback mpc = new MapParametersCallback(map);
        s.setParameters(mpc);
        System.out.println("s.getBson() = " + s.getOperation().getFirstArgumentAsBson());
    }
}
