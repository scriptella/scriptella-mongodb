package org.scriptella.mongodb.statement;

import com.mongodb.util.JSON;
import com.mongodb.util.JSONCallback;
import com.mongodb.util.JSONParseException;
import org.bson.BSONObject;
import org.scriptella.mongodb.ExpressionMatcher;
import org.scriptella.mongodb.MongoDbProviderException;
import scriptella.expression.Expression;
import scriptella.spi.ParametersCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a compiled BSON statement.
 * Compilation is done at construction time. After construction only parameters are substituted using {@link #setParameters(scriptella.spi.ParametersCallback)} methods.
 * This approach has a significant performance benefit over parsing JSON on each invocation.
 * <h3>Parameters syntax</h3>
 * <code><b>"?</b>variable<b>"<b></code> or <code><b>"?</b>{variable or JEXL expression}<b>"</b></code> syntax is supported. Please note that variable/expressions bindings <em>must be enclosed in single or double quotes</em>.
 * This decision was made to keep parameterized JSON compatible with plain JSON, so that standard parsers can read it.
 * <p/>
 * <p><b>Note: </b>This class is not thread-safe</p>
 *
 * @author Fyodor Kupolov <scriptella@gmail.com>
 */
public class BsonStatement {
    private MongoOperation operation;
    List<ObjectBindings> bindings;

    /**
     * Creates a BSON statement from JSON.
     *
     * @param json json statement
     * @throws MongoDbProviderException if compilation fails
     */
    public BsonStatement(String json)
            throws MongoDbProviderException {
        parse(json);
    }

    void parse(String json) {
        ParserCallback parserCallback = new ParserCallback();

        try {
            operation = MongoOperation.from((BSONObject) JSON.parse(json, parserCallback));
            bindings = parserCallback.bindings;
        } catch (JSONParseException e) {
            throw new MongoDbProviderException("Unable to from JSON statement", e);
        }
    }

    public void setParameters(final ParametersCallback params) {
        if (bindings != null) {
            for (ObjectBindings binding : bindings) {
                if (binding.bindingsList != null) {
                    for (ObjectBinding vb : binding.bindingsList) {
                        binding.object.put(vb.property,
                                vb.evaluate(params));
                    }
                }
            }
        }
    }

    public MongoOperation getOperation() {
        return operation;
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
        String property;
        String variableName;
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
    }
}
