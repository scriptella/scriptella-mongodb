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

    @Test
    /**
     * Test for #4 Allow passing user/password as attributes of the connection element
     */
    public void testPrependUserPasswordToUrl() {
        String url = "mongodb://localhost/test";
        String actual = MongoBridgeFactory.prependUserPasswordToUrl(url, null, null);
        Assert.assertEquals(url, actual);
        actual = MongoBridgeFactory.prependUserPasswordToUrl(url, "user", null);
        Assert.assertEquals("mongodb://user@localhost/test", actual);
        actual = MongoBridgeFactory.prependUserPasswordToUrl(url, "user", "password");
        Assert.assertEquals("mongodb://user:password@localhost/test", actual);

        //Malformed url without a protocol etc., should be returned unmodified
        String badUrl = "localhost/test";
        actual = MongoBridgeFactory.prependUserPasswordToUrl(badUrl, "user", "password");
        Assert.assertEquals(badUrl, actual);
    }

}
