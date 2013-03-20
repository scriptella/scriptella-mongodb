package org.scriptella.mongodb.statement.operation;

import com.mongodb.DBObject;
import org.scriptella.mongodb.statement.MongoBridge;
import org.scriptella.mongodb.statement.MongoOperation;

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
    public void execute(MongoBridge mongoBridge) {
        mongoBridge.find(getCollectionName(), (DBObject) getFirstArgumentAsBson());
    }
}
