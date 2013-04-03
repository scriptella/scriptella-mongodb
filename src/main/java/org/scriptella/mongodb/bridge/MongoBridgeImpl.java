package org.scriptella.mongodb.bridge;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class MongoBridgeImpl implements MongoBridge {
    private static final Logger LOG = Logger.getLogger(MongoBridgeImpl.class.getName());
    private static final boolean DEBUG = LOG.isLoggable(Level.FINE);
    MongoClient client;
    DB db;
    private String defaultCollection;

    public MongoBridgeImpl() {
    }

    public MongoBridgeImpl(MongoClient client, DB db, String defaultCollection) {
        this.client = client;
        this.db = db;
        this.defaultCollection = defaultCollection;
    }

    @Override
    public void runCommand(DBObject object) {
        db.command(object);
        if (DEBUG) {
            LOG.fine("Executing command " + object);
        }
    }

    @Override
    public DBCursor find(String collection, DBObject ref) {
        if (DEBUG) {
            LOG.fine("Executing MongoDB find for collection " + collection + ", data: " + ref);
        }
        return db.getCollection(collection).find(ref);
    }

    @Override
    public void save(String collection, DBObject ref) {
        if (DEBUG) {
            LOG.fine("Executing MongoDB save for collection " + collection + ", data: " + ref);
        }
        db.getCollection(collection).save(ref);
    }

    @Override
    public void close() {
        if (client != null) {
            client.close();
            db = null;
            client = null;
        }
    }
}
