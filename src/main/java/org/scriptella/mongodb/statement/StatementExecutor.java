package org.scriptella.mongodb.statement;

import com.mongodb.DBObject;
import org.scriptella.mongodb.MongoDbProviderException;
import scriptella.spi.ParametersCallback;
import scriptella.spi.Resource;
import scriptella.util.IOUtils;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class StatementExecutor {
    private Map<Resource, BsonStatement> cache = new IdentityHashMap<Resource, BsonStatement>();
    private MongoBridge bridge = new MongoBridge();


    public void executeScript(Resource resource, ParametersCallback parametersCallback) {
        BsonStatement statement = compile(resource);
        MongoOperation operation = statement.getOperation();
        if (MongoOperation.DB_RUN_COMMAND.equals(operation.getName())) {
            bridge.runCommand((DBObject) operation.getFirstArgumentAsBson());
        } else {
            throw new UnsupportedOperationException(operation + " is not supported");
        }
    }

    BsonStatement compile(Resource resource) {
        BsonStatement statement = cache.get(resource);
        if (statement == null) {
            try {
                statement = new BsonStatement(IOUtils.toString(resource.open()));
            } catch (IOException e) {
                throw new MongoDbProviderException("Failed to read JSON resource", e);
            }
            cache.put(resource, statement);
        }
        return statement;
    }

}
