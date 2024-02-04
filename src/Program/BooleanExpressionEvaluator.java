/**
 * 本代码 ChatGPT 编写
 */

package Program;

public class BooleanExpressionEvaluator {
    /**
     *
     */
    private String expression;
    private int index;

    public boolean evaluate(String expression) {
        this.expression = expression;
        this.index = 0;
        return parseExpression();
    }

    private boolean parseExpression() {
        boolean value = parseTerm();

        while (index < expression.length()) {
            char operator = expression.charAt(index);
            if (operator != '&' && operator != '|' && operator != '<' && operator != '>') {
                break;
            }
            index++;

            boolean nextValue = parseTerm();

            if (operator == '&') {
                value &= nextValue;
            } else if (operator == '|') {
                value |= nextValue;
            } else if (operator == '<') {
                value = compareValues(value, nextValue, true);
            } else if (operator == '>') {
                value = compareValues(value, nextValue, false);
            }
        }

        return value;
    }

    private boolean parseTerm() {
        char c = expression.charAt(index);

        if (c == '(') {
            index++;
            boolean value = parseExpression();
            if (expression.charAt(index) != ')') {
                throw new IllegalArgumentException("Invalid expression: " + expression);
            }
            index++;
            return value;
        } else if (Character.isDigit(c)) {
            double number = parseNumber();
            return (number != 0.0);
        } else if (c == 't' || c == 'T') {
            index++;
            return true;
        } else if (c == 'f' || c == 'F') {
            index++;
            return false;
        } else {
            throw new IllegalArgumentException("Invalid expression: " + expression);
        }
    }

    private double parseNumber() {
        StringBuilder sb = new StringBuilder();
        while (index < expression.length() && (Character.isDigit(expression.charAt(index)) || expression.charAt(index) == '.')) {
            sb.append(expression.charAt(index));
            index++;
        }
        try {
            return Double.parseDouble(sb.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number: " + sb.toString());
        }
    }

    private boolean compareValues(boolean value1, boolean value2, boolean lessThan) {
        double number1 = value1 ? 1.0 : 0.0;
        double number2 = value2 ? 1.0 : 0.0;

        if (lessThan) {
            return number1 < number2;
        } else {
            return number1 > number2;
        }
    }
}