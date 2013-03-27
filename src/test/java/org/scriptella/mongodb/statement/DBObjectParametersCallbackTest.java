package org.scriptella.mongodb.statement;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import junit.framework.Assert;
import org.junit.Test;
import org.scriptella.mongodb.test.AbstractTestCase;
import scriptella.spi.support.MapParametersCallback;

import java.util.Collections;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class DBObjectParametersCallbackTest extends AbstractTestCase {
    @Test
    public void testSimple() {
        DBObject o = new BasicDBObject();
        o.put("value", "V");
        DBObjectParametersCallback dpc = new DBObjectParametersCallback(new MapParametersCallback(Collections.singletonMap("parentVar", "parent")));
        dpc.setObject(o);
        Assert.assertEquals("V", dpc.getParameter("value"));
        Assert.assertEquals("parent", dpc.getParameter("parentVar"));
        Assert.assertNull(dpc.getParameter("noSuchVar"));
    }

    @Test
    public void testArray() {
        //TODO test bean.value[1]
        DBObject o = new BasicDBObject();
        DBObject bean = new BasicDBObject();
        BasicDBList arr = new BasicDBList();
        arr.add("V1");
        arr.add("V2");
        bean.put("value", arr);
        o.put("bean", bean);
    }


}
