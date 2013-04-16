package org.scriptella.mongodb.operation;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.bson.BSONObject;
import org.scriptella.mongodb.bridge.MongoBridge;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents MongoDB command or method.
 *
 * @author Fyodor Kupolov <scriptella@gmail.com>
 */
public abstract class MongoOperation {

    public static final String OPERATION_PARAM = "operation";
    public static final String COLLECTION_PARAM = "collection";
    public static final String DATA_PARAM = "data";
    /**
     * Executes DB command
     */
    public static String NAME = "db.runCommand";


    private String collectionName;
    List<Object> arguments;

    public MongoOperation(List<Object> arguments) {
        this(null, arguments);
    }

    public MongoOperation(String collectionName, List<Object> arguments) {
        this.collectionName = collectionName;
        this.arguments = Collections.unmodifiableList(arguments);
    }

    public String getCollectionName() {
        return collectionName;
    }

    public List<Object> getArguments() {
        return arguments;
    }

    /**
     * the first argument cast to {@link BSONObject}. Can be used to avoid boilerplate code when operation is known to have only one argument of {@link BSONObject}.
     *
     * @return the first argument cast to {@link BSONObject}.
     */
    @SuppressWarnings("unchecked")
    public <T extends BSONObject> T getFirstArgumentAsBson() {
        return (T) arguments.get(0);
    }

    /**
     * Returns a deep copy of the object.
     * <b>Note:</b> Currently only istances of {@link BasicDBObject} are supported.
     *
     * @param object object to clone
     * @return deep copy of the object.
     */
    protected BSONObject getDeepCopy(BSONObject object) {
        //TODO Implement an alternative deep copying if necessary
        if (object == null) {
            return null;
        }
        if (object instanceof BasicDBList) {
            return (BSONObject) ((BasicDBList) object).copy();
        } else if (object instanceof BasicDBObject) {
            return (BSONObject) ((BasicDBObject) object).copy();
        }
        throw new IllegalArgumentException("Type " + object.getClass() + " cannot be deep copied.");
    }

    public abstract void executeScript(MongoBridge mongoBridge);

    public abstract DBCursor executeQuery(MongoBridge mongoBridge);


    @Override
    public String toString() {
        return "MongoOperation{" +
                ", collectionName='" + collectionName + '\'' +
                ", arguments=" + arguments +
                '}';
    }

    public static MongoOperation from(BSONObject object) {
        Object name = object.get(OPERATION_PARAM);
        Object collection = object.get(COLLECTION_PARAM);
        Object data = object.get(DATA_PARAM);
        if ("db.collection.find".equals(name)) {
            return new DbCollectionFind((String) collection, (DBObject) data);
        } else if ("db.runCommand".equals(name)) {
            return new DbRunCommand((DBObject) data);
        } else if ("db.collection.save".equals(name)) {
            return new DbCollectionSave((String) collection, Arrays.asList(data));
        } else if ("db.collection.update".equals(name)) {
            return new DbCollectionUpdate((String) collection, (DBObject) data);
        }

        throw new UnsupportedOperationException("Operation " + name + " is not supported");

    }
}
