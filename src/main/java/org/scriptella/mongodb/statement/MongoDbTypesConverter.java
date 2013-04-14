package org.scriptella.mongodb.statement;

import org.scriptella.mongodb.MongoDbProviderException;
import scriptella.util.IOUtils;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Converter for objects passed to and from MongoDB.
 * <p>Some types link {@link Clob}/{@link Blob} needs conversion before being passed to MongoDB.</p>
 *
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class MongoDbTypesConverter {
    //Since BLOBS/CLOBS are stored in memory, use a reasonable default limit of 64MB
    static long MAX_LOB_SIZE = Long.getLong("MongoDbTypesConverter.MAX_LOB_SIZE", 64 * 1024 * 1024);

    public Object toMongoDb(Object object) {
        if (object instanceof Clob) {
            try {
                return IOUtils.toString(((Clob) object).getCharacterStream(), MAX_LOB_SIZE);
            } catch (IOException e) {
                throw new MongoDbProviderException("Cannot convert clob to string", e);
            } catch (SQLException e) {
                throw new MongoDbProviderException("Cannot convert clob to string", e);
            }
        }
        if (object instanceof Blob) {
            try {
                return IOUtils.toByteArray(((Blob) object).getBinaryStream(), MAX_LOB_SIZE);
            } catch (IOException e) {
                throw new MongoDbProviderException("Cannot convert clob to string", e);
            } catch (SQLException e) {
                throw new MongoDbProviderException("Cannot convert clob to string", e);
            }
        }
        return object;
    }
}
