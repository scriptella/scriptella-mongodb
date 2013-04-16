package org.scriptella.mongodb.bridge;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Abstraction for direct MongoDB invocations.
 *
 * @author Fyodor Kupolov
 * @version 1.0
 */
public interface MongoBridge extends AutoCloseable {
    /**
     * Runs a specified database command.
     *
     * @param object document representing the command.
     */
    void runCommand(DBObject object);

    DBCursor find(String collection, DBObject ref);

    void save(String collection, DBObject ref);

    void update(String collection, DBObject queryDoc, DBObject updateDoc, boolean upsert, boolean multi);

    @Override
    void close();
}
