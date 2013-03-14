package org.scriptella.mongodb;

import scriptella.driver.csv.CsvConnection;
import scriptella.spi.AbstractScriptellaDriver;
import scriptella.spi.Connection;
import scriptella.spi.ConnectionParameters;
import scriptella.spi.DialectIdentifier;

/**
 * Scriptella Driver for MongoDB
 *
 * @author Fyodor Kupolov
 */
public class Driver extends AbstractScriptellaDriver {
    static final DialectIdentifier DIALECT = new DialectIdentifier("MongoDB", "1.0");

    public Connection connect(ConnectionParameters connectionParameters) {
        return null;
    }
}