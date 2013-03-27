package org.scriptella.mongodb.statement;

import com.mongodb.DBObject;
import scriptella.spi.ParametersCallback;

/**
 * Represents a parameters callback for accessing properties of {@link DBObject}.
 *
 * @author Fyodor Kupolov
 * @version 1.0
 */
public class DBObjectParametersCallback implements ParametersCallback {
    private ParametersCallback parent;
    private DBObject object;

    public DBObjectParametersCallback(ParametersCallback parent) {
        this.parent = parent;
    }

    public void setObject(DBObject object) {
        this.object = object;
    }

    @Override
    public Object getParameter(String name) {
        //TODO Add support for nested properties. Consider JEXL or an own implementation
        Object o = object.get(name);
        return o == null ? parent.getParameter(name) : o;
    }
}
