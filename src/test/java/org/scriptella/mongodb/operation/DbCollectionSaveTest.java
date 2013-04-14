package org.scriptella.mongodb.operation;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import junit.framework.Assert;
import org.junit.Test;
import org.scriptella.mongodb.bridge.MongoMockBridge;
import org.scriptella.mongodb.test.AbstractTestCase;

import java.util.Arrays;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class DbCollectionSaveTest extends AbstractTestCase {

    @Test
    /**
     * Test for issue #5 Clone object in db.collection.save
     */
    public void testExecuteScript() throws Exception {
        //Reuse the same DBObject and make sure it is cloned before passing to MongoBridge
        DBObject o = new BasicDBObject();
        o.put("p", "1");
        DbCollectionSave save = new DbCollectionSave("test", Arrays.<Object>asList(o));
        MongoMockBridge b = new MongoMockBridge();
        save.executeScript(b);
        o.put("p", "2");
        save.executeScript(b);
        //Check that 2 commands were sent and the arguments are different
        Assert.assertEquals(2, b.getCommands().size());
        Assert.assertEquals("save", b.getCommands().get(0));
        DBObject a1 = (DBObject) b.getArgs().get(0);
        DBObject a2 = (DBObject) b.getArgs().get(1);
        Assert.assertEquals("1", a1.get("p"));
        Assert.assertEquals("2", a2.get("p"));
    }
}
