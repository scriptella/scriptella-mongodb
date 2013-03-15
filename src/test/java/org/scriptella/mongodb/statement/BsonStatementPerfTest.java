package org.scriptella.mongodb.statement;

import com.mongodb.util.JSON;
import org.junit.BeforeClass;
import org.junit.Test;
import scriptella.spi.support.MapParametersCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Perf test for {@link BsonStatement}
 * @author fkupolov
 */
public class BsonStatementPerfTest {
    private static final Logger LOG = Logger.getLogger(BsonStatement.class.getName());
    private static String js =
            "{\n" + "  '_id': '10280',\n" + "  'city': '?{city}',\n" + "  \"state\": \"?state\",\n" + "  \"pop\": 5574,\n" +
                    "  \"loc\": [\n" + "    '?loc1',\n" + "    40.710537\n" + "  ]\n" + "}";

    @BeforeClass
    public static void setup() {
        LOG.info("Warming up");
        replaceAndParse(5000);
        preparedStmt(5000);
    }

    @Test
    /**
     * Run history:
     * March 2013. Intel i5 2.50GHz -0.33s
     */
    public void testReplaceAndParse() {
        replaceAndParse(50000);
    }

    @Test
    /**
     * Run history:
     * March 2013. Intel i5 2.50GHz -0.045s
     */
    public void testBsonStatement() {
        preparedStmt(50000);
    }

    static long replaceAndParse(int runCount) {
        int v = 0;

        for (int i = 0; i < runCount; i++) {
            String s = js.replace("?{city}", "CITY");
            s = s.replace("?state", "STATE");
            s = s.replace("?loc1", "11.11111");

            final Map o = (Map) JSON.parse(s);
            v += o.size();
        }

        return v;
    }

    static long preparedStmt(int runCount) {
        int v = 0;
        BsonStatement bs = new BsonStatement(js);

        for (int i = 0; i < runCount; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("city", "CITY");
            map.put("state", "STATE");
            map.put("loc1", 11.11111);
            bs.setParameters(new MapParametersCallback(map));

            v += ((Map) bs.getBson()).size();
        }

        return v;
    }
}
