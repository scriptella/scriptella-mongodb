package org.scriptella.mongodb;

import org.junit.Assert;
import org.junit.Test;
import org.scriptella.mongodb.test.AbstractTestCase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class MongoBridgeFactoryTest extends AbstractTestCase {
    @Test
    public void test() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("p1", "v1");
        params.put("escaped=", " = ");
        String url = "mongodb://localhost/test";
        String result = MongoBridgeFactory.appendPropertiesToUrl(params, url);
        Assert.assertEquals("mongodb://localhost/test?p1=v1&escaped%3D=+%3D+", result);

        result = MongoBridgeFactory.appendPropertiesToUrl(Collections.<String, Object>emptyMap(), url);
        Assert.assertEquals("mongodb://localhost/test", result);
    }
}
