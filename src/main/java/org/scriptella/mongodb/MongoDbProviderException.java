package org.scriptella.mongodb;

import scriptella.spi.ProviderException;

/**
 * @author Fyodor Kupolov <scriptella@gmail.com>
 */
public class MongoDbProviderException
        extends ProviderException {
    public MongoDbProviderException() {
    }

    public MongoDbProviderException(String message) {
        super(message);
    }

    public MongoDbProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public MongoDbProviderException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getProviderName() {
        return Driver.DIALECT.getName();
    }
}
