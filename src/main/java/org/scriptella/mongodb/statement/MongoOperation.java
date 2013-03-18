package org.scriptella.mongodb.statement;

import org.bson.BSONObject;

import java.util.Collections;
import java.util.List;

/**
 * Represents MongoDB command or method.
 *
 * @author Fyodor Kupolov <scriptella@gmail.com>
 */
public class MongoOperation {
    /**
     * Executes DB command
     */
    public static String DB_RUN_COMMAND = "db.runCommand";
    /**
     * Represents JSON document which can be executed as find or update depending on the context.
     */
    public static String RUN_JSON_DOC = "scriptella.document";

    private String name;
    List<Object> arguments;

    public MongoOperation(String name, List<Object> arguments) {
        this.name = name;
        this.arguments = Collections.unmodifiableList(arguments);
    }

    public String getName() {
        return name;
    }

    public List<Object> getArguments() {
        return arguments;
    }

    /**
     * the first argument cast to {@link BSONObject}. Can be used to avoid boilerplate code when operation is known to have only one argument of {@link BSONObject}.
     *
     * @return the first argument cast to {@link BSONObject}.
     */
    public BSONObject getFirstArgumentAsBson() {
        return (BSONObject) arguments.get(0);
    }

    public static MongoOperation from(BSONObject object) {
        //TODO For now only #RUN_JSON_DOC is hardcoded. This has to be extended to check for $scriptellaMethod JSON extension
        return new MongoOperation(RUN_JSON_DOC, Collections.singletonList((Object) object));
    }
}
