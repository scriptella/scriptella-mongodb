package org.scriptella.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.scriptella.mongodb.statement.MongoBridge;
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
        executor = new StatementExecutor(initBridge(parameters));
    }

    MongoBridge initBridge(ConnectionParameters parameters) {
        MongoClientURI uri;
        MongoClient mc;
        try {
            uri = new MongoClientURI(parameters.getUrl());
            mc = new MongoClient(uri);
        } catch (Exception e) {
            throw new MongoDbProviderException("Invalid URI " + parameters.getUrl(), e);
        }

        String database = uri.getDatabase();

        MongoBridge mb = new MongoBridge(mc, mc.getDB(database == null ? "admin" : database), uri.getCollection());
        return mb;
    }

    String addConnectionPropertiesToUrl(ConnectionParameters parameters) {
        //Append Add user/password or properties to the url
        //The existing values in the url takes precedence over parameters.
        return parameters.getUrl();
    }


    @Override
    public void executeScript(Resource scriptContent, ParametersCallback parametersCallback)
            throws ProviderException {
        executor.executeScript(scriptContent, parametersCallback);
    }

    @Override
    public void executeQuery(Resource queryContent, ParametersCallback parametersCallback, QueryCallback queryCallback)
            throws ProviderException {
        throw new UnsupportedOperationException();
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
