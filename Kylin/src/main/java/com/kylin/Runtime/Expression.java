package com.kylin.Runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Expression
{
    public static String getExString(String content , int line,MainRuntime mainRuntime)
    {
        try
        {
            /**
             * Split all the token.
             */
            StringBuffer stringBuffer = new StringBuffer();
            boolean isStr = false;
            String[] tokens = content.split("(?<=<\\s)(.*?)(?=\\s>)");

            for (int i = 0 ; i < tokens.length ;i++)
            {
                String token = tokens[i].trim();
                //System.out.println(token);
                if (token.startsWith("\"") && token.endsWith("\"")) {
                    stringBuffer.append(token, 1, token.length() -1);
                }
                else if (token.startsWith("\"")) {
                    isStr = true;
                    stringBuffer.append(token.substring(1));
                    continue;
                }
                else if (isStr && token.endsWith("\"")) {
                    isStr = false;
                    stringBuffer.append(token, 0, token.length() - 1);
                    continue;
                }
                else if (token.startsWith("<") && token.endsWith(">"))
                {
                    //System.out.println(token);
                    //Calculator calculator = new Calculator();
                    String input = token.substring(1, token.length() -1);
                    input = input.replaceAll("\\s+", "");
                    StringBuffer sBuffer = new StringBuffer();
                    for (String s : input.split("\\s*(?<=[\\+\\-\\*/])|(?=[\\+\\-\\*/])\\s*"))
                    {
                        if (mainRuntime.ValueMap.containsKey(s.trim()))
                        {
                            //System.out.println(mainRuntime.ValueMap.get(s.trim()).getContent());
                            sBuffer.append(mainRuntime.ValueMap.get(s.trim()).getContent());
                            continue;
                        }
                        sBuffer.append(s);
                    }
                    input = sBuffer.toString();
                    //System.out.println(input);
                
                    List<String> postfix = Calculator.toPostfix(input);
                    stringBuffer.append(Calculator.calculate(postfix));
                    continue;
                } 
                else if (mainRuntime.ValueMap.containsKey(token))
                {
                    stringBuffer.append(mainRuntime.ValueMap.get(token).getContent());
                    continue;
                }
                else {
                    stringBuffer.append(token);
                }
            }
            return stringBuffer.toString();
        }
        catch (Exception exception)
        {
            //exception.printStackTrace();
            MainRuntime.sendSyntaxError(exception.getMessage() , line);
            return null;
        }
    }
}
class Calculator {
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