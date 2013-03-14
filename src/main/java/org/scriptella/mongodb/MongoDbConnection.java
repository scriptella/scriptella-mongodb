package org.scriptella.mongodb;

import scriptella.spi.*;

/**
 * @author Fyodor Kupolov
 */
public class MongoDbConnection extends AbstractConnection {
    @Override
    public void executeScript(Resource scriptContent, ParametersCallback parametersCallback) throws ProviderException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void executeQuery(Resource queryContent, ParametersCallback parametersCallback, QueryCallback queryCallback) throws ProviderException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws ProviderException {

    }
}
