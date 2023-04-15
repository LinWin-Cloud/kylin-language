package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;
import com.kylin.Main;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.Locale;

public class Expression {
    public static String getExpression(String input,int line) {
        try {
            String tmp = input.replaceAll("\\s","");
            List<String> tokens = tokenize(tmp);
            for (int i = 0 ; i < tokens.size() ; i++) {
                Value value = MainRuntime.value.get(tokens.get(i).trim());
                if (value == null) {
                    continue;
                }
                else {
                    tokens.set(i , value.value);
                }
            }
            BigDecimal result = calculate(tokens,line);
            return String.valueOf(result);
        }
        catch (Exception exception){
            MainRuntime.sendRuntimeError(exception.getMessage(),line);
        }

        try {
            return input;
        }
        catch (Exception exception) {
            //exception.printStackTrace();
            MainRuntime.sendSyntaxError("Input expression error",line);
            return null;
        }
    }
    public static boolean isDouble(String value) {
        try {
            double d = Double.parseDouble(value);
            return true;
        }
        catch (Exception exception)
        {
            return false;
        }
    }
    public static List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (sb.length() > 0) {
                    tokens.add(sb.toString());
                    sb.setLength(0);
                }
                tokens.add(Character.toString(c));
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            tokens.add(sb.toString());
        }
        return tokens;
    }

    /**
     * calculate the expression's value
     */
    public static BigDecimal calculate(List<String> tokens,int line) {
        if (tokens.size() == 1) {
            return new BigDecimal(tokens.get(0));
        }

        int index = findOperatorIndex(tokens,line);
        String operator = tokens.get(index);
        List<String> leftTokens = tokens.subList(0, index);
        List<String> rightTokens = tokens.subList(index + 1, tokens.size());
        BigDecimal operand1 = calculate(leftTokens,line);
        BigDecimal operand2 = calculate(rightTokens,line);

        BigDecimal result = null;
        switch (operator) {
            case "+":
                result = operand1.add(operand2);
                break;
            case "-":
                result = operand1.subtract(operand2);
                break;
            case "*":
                result = operand1.multiply(operand2);
                break;
            case "/":
                result = operand1.divide(operand2, 10, BigDecimal.ROUND_HALF_UP);
                break;
            default:
                MainRuntime.sendSyntaxError("Cannot find Operator number:" + operator,line);
        }

        return result;
    }

    /**
     * find the expression's first Operator number's location.
     */
    public static int findOperatorIndex(List<String> tokens,int line) {
        int index = -1;
        int level = 0;
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.equals("(")) {
                level++;
            } else if (token.equals(")")) {
                level--;
            } else if (isOperator(token) && level == 0) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            MainRuntime.sendSyntaxError("Cannot find Operator number",line);
        }
        return index;
    }

    public static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }
}
