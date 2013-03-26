package org.scriptella.mongodb.statement.operation;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.scriptella.mongodb.statement.MongoBridge;
import org.scriptella.mongodb.statement.MongoOperation;

import java.util.Arrays;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class DbRunCommand extends MongoOperation {

    public DbRunCommand(DBObject command) {
        super(null, Arrays.<Object>asList(command));
    }

    @Override
    public void executeScript(MongoBridge mongoBridge) {
        mongoBridge.runCommand((DBObject) getFirstArgumentAsBson());
    }

    @Override
    public DBCursor executeQuery(MongoBridge mongoBridge) {
        throw new UnsupportedOperationException("runCommand operation is not supported in queries");
    }


}
