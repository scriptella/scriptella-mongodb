package org.scriptella.mongodb.bridge;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Testable mock version of {@MongoBridge}
 *
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class MongoMockBridge implements MongoBridge {
    private List<String> commands = new ArrayList<String>();
    private List<Object> args = new ArrayList<Object>();
    private boolean closed;

    @Override
    public void runCommand(DBObject object) {
        commands.add("runCommand");
        args.add(object);
    }

    @Override
    public DBCursor find(String collection, DBObject ref) {
        commands.add("find");
        args.add(ref);
        return null;
    }

    @Override
    public void save(String collection, DBObject ref) {
        commands.add("save");
        args.add(ref);
    }

    @Override
    public void close() throws Exception {
        closed = true;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<Object> getArgs() {
        return args;
    }

    public boolean isClosed() {
        return closed;
    }
}
