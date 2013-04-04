package org.scriptella.mongodb.statement;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Fyodor Kupolov <scriptella@gmail.com>
 */
public class ExpressionMatcherTest {
    @Test
    public void test() {
        final String s = "test $var and ${expr}";
        ExpressionMatcher m = new ExpressionMatcher(s);
        Assert.assertTrue(m.matches());
        Assert.assertFalse(m.isExpression());
        Assert.assertEquals("test",
                m.getMatchString());

        m.setFrom(s.indexOf('$') + 1);
        Assert.assertTrue(m.matches());
        Assert.assertFalse(m.isExpression());
        Assert.assertEquals("var",
                m.getMatchString());
        m.setFrom(s.indexOf('$', 10) + 1);
        Assert.assertTrue(m.matches());
        Assert.assertTrue(m.isExpression());
        Assert.assertEquals("expr",
                m.getMatchString());
    }

    @Test
    public void testWithDots() {
        final String s = "test.$var.and ${expr}.";
        ExpressionMatcher m = new ExpressionMatcher(s);
        Assert.assertTrue(m.matches());
        Assert.assertFalse(m.isExpression());
        Assert.assertEquals("test",
                m.getMatchString());

        m.setFrom(s.indexOf('$') + 1);
        Assert.assertTrue(m.matches());
        Assert.assertFalse(m.isExpression());
        Assert.assertEquals("var",
                m.getMatchString());
        m.setFrom(s.indexOf('$', 10) + 1);
        Assert.assertTrue(m.matches());
        Assert.assertTrue(m.isExpression());
        Assert.assertEquals("expr",
                m.getMatchString());
    }

}
