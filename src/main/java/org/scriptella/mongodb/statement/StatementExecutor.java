package org.scriptella.mongodb.statement;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.scriptella.mongodb.MongoDbProviderException;
import org.scriptella.mongodb.bridge.MongoBridgeImpl;
import org.scriptella.mongodb.operation.MongoOperation;
import scriptella.spi.ParametersCallback;
import scriptella.spi.QueryCallback;
import scriptella.spi.Resource;
import scriptella.util.IOUtils;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class StatementExecutor implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(StatementExecutor.class.getName());
    private static final boolean DEBUG = LOG.isLoggable(Level.FINE);
    private Map<Resource, BsonStatement> cache = new IdentityHashMap<Resource, BsonStatement>();
    private MongoBridgeImpl bridge;

    StatementExecutor() {
    }

    public StatementExecutor(MongoBridgeImpl bridge) {
        this.bridge = bridge;
    }

    public void executeScript(Resource resource, ParametersCallback parametersCallback) {
        BsonStatement statement = compile(resource);
        statement.setParameters(parametersCallback);
        MongoOperation operation = statement.getOperation();
        if (DEBUG) {
            LOG.fine("Executing operation " + operation);
        }
        operation.executeScript(bridge);
    }

    public void executeQuery(Resource resource, ParametersCallback parametersCallback, QueryCallback queryCallback) {
        BsonStatement statement = compile(resource);
        statement.setParameters(parametersCallback);
        MongoOperation operation = statement.getOperation();
        if (DEBUG) {
            LOG.fine("Executing query " + operation);
        }
        DBCursor cursor = operation.executeQuery(bridge);
        DBObjectParametersCallback row = new DBObjectParametersCallback(parametersCallback);
        try {
            for (DBObject dbObject : cursor) {
                row.setObject(dbObject);
                queryCallback.processRow(row);
            }
        } finally {
            IOUtils.closeSilently(cursor);
        }
    }


    BsonStatement compile(Resource resource) {
        BsonStatement statement = cache.get(resource);
        if (statement == null) {
            try {
                statement = new BsonStatement(IOUtils.toString(resource.open()));

                if (DEBUG) {
                    LOG.fine("Compiled statement " + statement);
                }
            } catch (IOException e) {
                throw new MongoDbProviderException("Failed to read JSON resource", e);
            }
            cache.put(resource, statement);
        }
        return statement;
    }

    @Override
    public void close() {
        if (bridge != null) {
            cache.clear();
            bridge.close();
            bridge = null;
        }
    }
}
