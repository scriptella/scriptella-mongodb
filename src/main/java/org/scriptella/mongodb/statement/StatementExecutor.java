package org.scriptella.mongodb.statement;

import org.scriptella.mongodb.MongoDbProviderException;
import scriptella.spi.ParametersCallback;
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
    private MongoBridge bridge = new MongoBridge();


    public void executeScript(Resource resource, ParametersCallback parametersCallback) {
        BsonStatement statement = compile(resource);
        statement.setParameters(parametersCallback);
        MongoOperation operation = statement.getOperation();
        if (DEBUG) {
            LOG.fine("Executing operation " + operation);
        }
        operation.execute(bridge);
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
    public void close() throws Exception {
        cache.clear();
        bridge = null;
    }
}