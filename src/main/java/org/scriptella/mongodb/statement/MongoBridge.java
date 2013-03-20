package org.scriptella.mongodb.statement;

import com.mongodb.DB;
import com.mongodb.DBObject;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class MongoBridge {
    DB db;

    public MongoBridge() {
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
//        db.getCollection(collection).save(ref);
        System.out.println("Executing save: Collection = " + collection + ", ref = " + ref);
    }

}
