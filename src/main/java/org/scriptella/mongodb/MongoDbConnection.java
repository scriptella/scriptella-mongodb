package org.scriptella.mongodb;

import org.scriptella.mongodb.statement.StatementExecutor;
import scriptella.spi.*;

/**
 * MongoDB connection adapter.
 *
 * @author Fyodor Kupolov <scriptella@gmail.com>
 */
public class MongoDbConnection
        extends AbstractConnection {

    private StatementExecutor executor;

    public MongoDbConnection(ConnectionParameters parameters) {
        super(Driver.DIALECT, parameters);
        executor = new StatementExecutor(new MongoBridgeFactory(parameters).newMongoBridge());
    }


    @Override
    public void executeScript(Resource scriptContent, ParametersCallback parametersCallback)
            throws ProviderException {
        executor.executeScript(scriptContent, parametersCallback);
    }

    @Override
    public void executeQuery(Resource queryContent, ParametersCallback parametersCallback, QueryCallback queryCallback)
            throws ProviderException {
        executor.executeQuery(queryContent, parametersCallback, queryCallback);
    }

    @Override
    public void close()
            throws ProviderException {
        if (executor != null) {
            executor.close();
            executor = null;
        }
    }
}
