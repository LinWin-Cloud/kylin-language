package com.kylin.Runtime;

import java.util.*;

public class Calculator {
    private static final Map<String, Integer> OPERATORS = new HashMap<>();
    static {
        OPERATORS.put("+", 1);
        OPERATORS.put("-", 1);
        OPERATORS.put("*", 2);
        OPERATORS.put("/", 2);
    }


    static List<String> toPostfix(String input) {
        List<String> postfix = new ArrayList<>();
        Stack<String> operators = new Stack<>();
        String[] tokens = input.split("(?<=[+\\-*/()])|(?=[+\\-*/()])");
        for (String token : tokens) {
            if (isOperator(token)) {
                while (!operators.isEmpty() && getPrecedence(operators.peek()) >= getPrecedence(token)) {
                    postfix.add(operators.pop());
                }
                operators.push(token);
            } else if (isNumber(token)) {
                postfix.add(token);
            } else if ("(".equals(token)) {
                operators.push(token);
            } else if (")".equals(token)) {
                while (!"(".equals(operators.peek())) {
                    postfix.add(operators.pop());
                }
                operators.pop();
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }
        while (!operators.isEmpty()) {
            postfix.add(operators.pop());
        }
        return postfix;
    }

    private static boolean isOperator(String token) {
        return OPERATORS.containsKey(token);
    }

    private static int getPrecedence(String operator) {
        return OPERATORS.getOrDefault(operator, 0);
    }

    private static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static double calculate(List<String> postfix) {
        Stack<Double> stack = new Stack<>();
        for (String token : postfix) {
            if (isOperator(token)) {
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(operand1 + operand2);
                        break;
                    case "-":
                        stack.push(operand1 - operand2);
                        break;
                    case "*":
                        stack.push(operand1 * operand2);
                        break;
                    case "/":
                        stack.push(operand1 / operand2);
                        break;
                }
            } else {
                stack.push(Double.parseDouble(token));
            }
        }
        return stack.pop();
    }
}