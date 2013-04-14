package org.scriptella.mongodb.operation;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.scriptella.mongodb.bridge.MongoBridge;

import java.util.List;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class DbCollectionSave extends MongoOperation {
    public DbCollectionSave(String collectionName, List<Object> arguments) {
        super(collectionName, arguments);
    }

    @Override
    public void executeScript(MongoBridge mongoBridge) {
        mongoBridge.save(getCollectionName(), (DBObject) getDeepCopy(getFirstArgumentAsBson()));
    }

    @Override
    public DBCursor executeQuery(MongoBridge mongoBridge) {
        throw new UnsupportedOperationException("Save operation is not supported in queries");
    }
}
