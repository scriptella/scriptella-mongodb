package org.scriptella.mongodb.operation;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.scriptella.mongodb.bridge.MongoBridgeImpl;

import java.util.Arrays;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class DbCollectionFind extends MongoOperation {
    public DbCollectionFind(String collectionName, DBObject arg) {
        super(collectionName, Arrays.<Object>asList(arg));
    }

    @Override
    public void executeScript(MongoBridgeImpl mongoBridge) {
        DBCursor dbObjects = executeQuery(mongoBridge);
        dbObjects.close();
    }

    @Override
    public DBCursor executeQuery(MongoBridgeImpl mongoBridge) {
        DBObject arg = getArguments().isEmpty() ? null : (DBObject) getArguments().get(0);
        return mongoBridge.find(getCollectionName(), arg);
    }
}
