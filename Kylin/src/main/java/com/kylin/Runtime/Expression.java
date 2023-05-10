package com.kylin.Runtime;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            String[] tokens = content.split("(?<=[<>])| ");


            for (int i = 0 ; i < tokens.length ;i++)
            {
                String token = tokens[i].trim();
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
                else if (mainRuntime.ValueMap.containsKey(token))
                {
                    stringBuffer.append(mainRuntime.ValueMap.get(token).getContent());
                    continue;
                }
                else if (token.startsWith("<")) {
                    stringBuffer.append(calculate(token.substring(1, token.length() -1)));
                }
                else {
                    stringBuffer.append(token);
                }
            }
            return stringBuffer.toString();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            MainRuntime.sendSyntaxError(exception.getMessage() , line);
            return null;
        }
    }
    public static double calculate(String expression) {
        expression = expression.replaceAll("\\s+", ""); // 去除空格
        String[] tokens = expression.split("(?<=[-+*/()])|(?=[-+*/()])"); // 分割字符串
        return evaluate(tokens); // 计算表达式
    }

    // 计算表达式的值
    public static double evaluate(String[] tokens) {
        String[] operators = {"+", "-", "*", "/"};
        for (String operator : operators) {
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].equals(operator)) {
                    double left = Double.parseDouble(tokens[i - 1]);
                    double right = Double.parseDouble(tokens[i + 1]);
                    double result = 0;
                    switch (operator) {
                        case "+":
                            result = left + right;
                            break;
                        case "-":
                            result = left - right;
                            break;
                        case "*":
                            result = left * right;
                            break;
                        case "/":
                            result = left / right;
                            break;
                    }
                    tokens[i - 1] = Double.toString(result);
                    for (int j = i; j < tokens.length - 2; j++) {
                        tokens[j] = tokens[j + 2];
                    }
                    tokens = java.util.Arrays.copyOf(tokens, tokens.length - 2);
                    i--;
                }
            }
        }
        return Double.parseDouble(tokens[0]);
    }
}