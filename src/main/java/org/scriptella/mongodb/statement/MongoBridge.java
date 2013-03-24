package org.scriptella.mongodb.statement;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class MongoBridge implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(MongoBridge.class.getName());
    private static final boolean DEBUG = LOG.isLoggable(Level.FINE);
    MongoClient client;
    DB db;
    private String defaultCollection;

    public MongoBridge() {
    }

    public MongoBridge(MongoClient client, DB db, String defaultCollection) {
        this.client = client;
        this.db = db;
        this.defaultCollection = defaultCollection;
    }

    public void runCommand(DBObject object) {
//        db.command(object);
        System.out.println("Executing command " + object);


    }

    public void find(String collection, DBObject ref) {
//        db.getCollection(collection).find(ref);
        System.out.println("Executing find: Collection " + collection + "," + ref);
    }

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
