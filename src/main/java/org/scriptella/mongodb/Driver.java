package org.scriptella.mongodb;

import scriptella.spi.AbstractScriptellaDriver;
import scriptella.spi.Connection;
import scriptella.spi.ConnectionParameters;
import scriptella.spi.DialectIdentifier;

/**
 * Scriptella Driver for MongoDB
 * <p>For configuration details and examples see <a href="https://github.com/scriptella/scriptella-mongodb">Documentation</a>.
 *
 * @author Fyodor Kupolov <scriptella@gmail.com>
 */
public class Driver
        extends AbstractScriptellaDriver {
    static final DialectIdentifier DIALECT = new DialectIdentifier("MongoDB", "1.0");

    public Connection connect(ConnectionParameters connectionParameters) {
        return null;
    }
}
