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

}
