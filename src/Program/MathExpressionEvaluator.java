package Program;

import java.util.Stack;

public class MathExpressionEvaluator {

    public static double evaluateExpression(String expression , KylinRuntime kylinRuntime) {
        String[] tokens = tokenizeExpression(expression);
        Stack<Double> stack = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                double operand2 = stack.pop();
                double operand1 = stack.pop();

                double value = performOperation(token, operand1, operand2);
                stack.push(value);
            }
        }

        return stack.pop();
    }

    private static String[] tokenizeExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");
        expression = expression.replaceAll("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)", " ");
        return expression.split("\\s+");
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isOperator(String str) {
        return str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/");
    }

    private static double performOperation(String operator, double operand1, double operand2) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Math Error");
        }
    }
}
