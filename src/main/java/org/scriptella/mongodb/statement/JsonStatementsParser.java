package org.scriptella.mongodb.statement;

import com.mongodb.util.JSON;
import com.mongodb.util.JSONCallback;
import com.mongodb.util.JSONParseException;
import org.bson.BSONObject;
import org.scriptella.mongodb.MongoDbProviderException;
import org.scriptella.mongodb.operation.MongoOperation;
import scriptella.expression.Expression;
import scriptella.spi.ParametersCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a parser for Mongodb statements in JSON format
 * .
 * Parsing and pre-compilation is done at construction time. After construction only parameters are substituted using {@link #setParameters(scriptella.spi.ParametersCallback)} methods.
 * This approach has a significant performance benefit over parsing JSON on each invocation.
 * <h3>Parameters syntax</h3>
 * <code><b>"?</b>variable<b>"<b></code> or <code><b>"?</b>{variable or JEXL expression}<b>"</b></code> syntax is supported. Please note that variable/expressions bindings <em>must be enclosed in single or double quotes</em>.
 * This decision was made to keep parameterized JSON compatible with plain JSON, so that standard parsers can read it.
 * <p/>
 * <p><b>Note: </b>This class is not thread-safe</p>
 *
 * @author Fyodor Kupolov <scriptella@gmail.com>
 */
public class JsonStatementsParser {
    private List<MongoOperation> operations;
    List<ObjectBindings> bindings;
    private MongoDbTypesConverter typesConverter;

    /**
     * Creates a BSON statement from JSON.
     *
     * @param json json statement
     * @throws MongoDbProviderException if compilation fails
     */
    public JsonStatementsParser(String json)
            throws MongoDbProviderException {
        parse(json);
    }

    void parse(String json) {
        ParserCallback parserCallback = new ParserCallback();

        try {

            BSONObject bson = (BSONObject) JSON.parse(json, parserCallback);
            operations = new ArrayList<MongoOperation>();
            if (bson instanceof List) {
                List<?> list = (List<?>) bson;
                for (Object o : list) {
                    if (o instanceof BSONObject) {
                        operations.add(MongoOperation.from((BSONObject) o));
                    } else {
                        throw new MongoDbProviderException("A document was expected in the array of operation, but was " + o);
                    }
                }
            } else {
                operations.add(MongoOperation.from(bson));

            }
            bindings = parserCallback.bindings;
        } catch (JSONParseException e) {
            throw new MongoDbProviderException("Unable to parse JSON statement", e);
        }
    }

    public void setTypesConverter(MongoDbTypesConverter typesConverter) {
        this.typesConverter = typesConverter;
    }

    public void setParameters(final ParametersCallback params) {
        if (bindings != null) {
            for (ObjectBindings binding : bindings) {
                if (binding.bindingsList != null) {
                    for (ObjectBinding vb : binding.bindingsList) {
                        Object value = vb.evaluate(params);
                        if (typesConverter != null) {
                            value = typesConverter.toMongoDb(value);
                        }
                        binding.object.put(vb.property, value);
                    }
                }
            }
        }
    }

    public List<MongoOperation> getOperations() {
        return operations;
    }

    static class ParserCallback
            extends JSONCallback {
        private List<ObjectBindings> stack = new ArrayList<ObjectBindings>();
        private List<ObjectBindings> bindings = new ArrayList<ObjectBindings>();
        private ExpressionMatcher matcher = new ExpressionMatcher();

        @Override
        public void objectStart() {
            onObjectStart();
            super.objectStart();
        }

        @Override
        public void objectStart(boolean array) {
            onObjectStart();
            super.objectStart(array);
        }

        @Override
        public void objectStart(String name) {
            onObjectStart();
            super.objectStart(name);
        }

        @Override
        public void objectStart(boolean array, String name) {
            onObjectStart();
            super.objectStart(array, name);
        }

        @Override
        public void gotString(String name, String v) {
            super.gotString(name, v);

            if ((v != null) && v.startsWith("?")) {
                matcher.reset(v);
                matcher.setFrom(1);

                if (matcher.matches()) {
                    final ObjectBindings current = peekObjectBindings();

                    if (current.bindingsList == null) {
                        current.bindingsList = new ArrayList<ObjectBinding>();
                    }

                    ObjectBinding ob;

                    if (matcher.isExpression()) {
                        ob = new ObjectBinding(name,
                                Expression.compile(matcher.getMatchString()));
                    } else {
                        ob = new ObjectBinding(name,
                                matcher.getMatchString());
                    }

                    current.bindingsList.add(ob);
                }
            }
        }

        private void onObjectStart() {
            stack.add(new ObjectBindings());
        }

        private ObjectBindings peekObjectBindings() {
            return stack.get(stack.size() - 1);
        }

        private void onObjectDone(Object object) {
            final ObjectBindings cur = stack.remove(stack.size() - 1);

            if (cur.bindingsList != null) {
                cur.object = (BSONObject) object;
                bindings.add(cur);
            }
        }

        @Override
        public Object objectDone() {
            final Object result = super.objectDone();
            onObjectDone(result);

            return result;
        }
    }

    List<ObjectBindings> getBindings() {
        return bindings;
    }

    static class ObjectBindings {
        BSONObject object;
        List<ObjectBinding> bindingsList;

        @Override
        public String toString() {
            return "ObjectBindings{" + "object=" + object + ", bindings=" + bindingsList + '}';
        }
    }

    static class ObjectBinding {
        /**
         * Name of the property of the JSON object to which the binding is assigned
         */
        String property;
        /**
         * Defined if it's a binding based on the variable, e.g. '?var'
         */
        String variableName;
        /**
         * Set if it's an expression binding, e.g. '?{expression}'
         */
        Expression expression;

        ObjectBinding(String property, String variableName) {
            this.property = property;
            this.variableName = variableName;
        }

        ObjectBinding(String property, Expression expression) {
            this.property = property;
            this.expression = expression;
        }

        public boolean isExpression() {
            return expression != null;
        }

        public Object evaluate(ParametersCallback params) {
            //Evaluate an expression or get parameter value
            return isExpression() ? expression.evaluate(params) : params.getParameter(variableName);
        }

        @Override
        public String toString() {
            return "ObjectBinding{" +
                    "property='" + property + '\'' +
                    ", variableName='" + variableName + '\'' +
                    ", expression=" + expression +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JsonStatementsParser{" +
                "operations=" + operations +
                ", bindings=" + bindings +
                '}';
    }
}
