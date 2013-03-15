package org.scriptella.mongodb;


/**
 * Matcher for variables and expressions used for properties substitution.
 * TODO: Move to scriptella core.
 *
 * @author Fyodor Kupolov <scriptella@gmail.com>
 */
public class ExpressionMatcher {
    private String line;
    private int from;
    private int end;
    private boolean expression;

    public ExpressionMatcher() {
    }

    public ExpressionMatcher(String line) {
        this.line = line;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getFrom() {
        return from;
    }

    /**
     * Attempt to match a variable name or expression starting from index {@link #getFrom()}.
     *
     * @return true if there is match.
     * @see #isExpression()
     * @see @getEnd()
     */
    public boolean matches() {
        if (line.charAt(from) == '{') {
            end = line.indexOf('}', from + 1) + 1;
            expression = true;

            return true;
        }

        expression = false;

        for (int i = from; i < line.length(); i++) {
            if (!isValidVariableChar(line.charAt(i))) {
                end = i;

                return i > from;
            }
        }

        end = line.length();

        return true;
    }

    /**
     * @return end of the last {@link #matches() match} exclusive
     * @see #matches()
     */
    public int getEnd() {
        return end;
    }

    /**
     * @return true if last {@link #matches() match} was an expression, otherwise it was a variable
     * @see #matches()
     */
    public boolean isExpression() {
        return expression;
    }

    public String getMatchString() {
        return line.substring(expression ? (from + 1) : from, expression ? (end - 1) : end);
    }

    public void reset() {
        from = 0;
        end = 0;
    }

    public void reset(String line) {
        reset();
        this.line = line;
    }

    private static boolean isValidVariableChar(char c) {
        return ((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z')) || (c == '_') ||
                ((c >= '0') && (c <= '9'));
    }
}
