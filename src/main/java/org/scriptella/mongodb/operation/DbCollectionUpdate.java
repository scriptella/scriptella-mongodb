package org.scriptella.mongodb.operation;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.bson.types.BasicBSONList;
import org.scriptella.mongodb.bridge.MongoBridge;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents <a href="http://docs.mongodb.org/manual/reference/method/db.collection.update/">db.collection.update</a> operation.
 *
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class DbCollectionUpdate extends MongoOperation {

    public DbCollectionUpdate(String collectionName, DBObject data) {
        super(collectionName, parse(data));
    }

    @Override
    public void executeScript(MongoBridge mongoBridge) {
        List<Object> args = getArguments();
        DBObject query = (DBObject) getDeepCopy((DBObject) args.get(0));
        DBObject update = (DBObject) getDeepCopy((DBObject) args.get(1));

        //Init to defaults
        boolean upsert = false;
        boolean multi = false;

        if (args.size() == 3) {
            DBObject options = (DBObject) args.get(2);
            upsert = Boolean.TRUE.equals(options.get("upsert"));
            multi = Boolean.TRUE.equals(options.get("multi"));
        } else if (args.size() == 4) {
            upsert = Boolean.TRUE.equals(args.get(2));
            multi = Boolean.TRUE.equals(args.get(3));
        }
        mongoBridge.update(getCollectionName(), query, update, upsert, multi);
    }

    @Override
    public DBCursor executeQuery(MongoBridge mongoBridge) {
        throw new UnsupportedOperationException("Update operation is not supported in queries");
    }

    private static List<Object> parse(DBObject data) {
        if (!(data instanceof BasicBSONList)) {
            throw new IllegalArgumentException("Array of arguments [query, update, [upsert], [multi]] was expected, but was " + data);
        }
        BasicBSONList list = (BasicBSONList) data;
        if (list.size() < 2 || list.size() > 4) {
            throw new IllegalArgumentException("Array of arguments [query, update, [upsert], [multi]] was expected, but was " + data);
        }
        return new ArrayList<Object>(list);
    }
}
