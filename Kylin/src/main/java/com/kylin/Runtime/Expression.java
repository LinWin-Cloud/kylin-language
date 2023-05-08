package com.kylin.Runtime;

public class Expression 
{
    public static String getExString(String content , int line) 
    {
        try
        {
            /**
             * Split all the token.
             */
            String output = "";
            String[] tokens = content.split(" ");
            for (int i = 1; i < tokens.length; i++) {
                String token = tokens[i];
                System.out.println(token);
                if (token.startsWith("\"")) {
                    // 字符串类型
                    output += token.substring(1, token.length() - 1);
                } else if (MainRuntime.ValueMap.containsKey(token)) {
                    // 变量类型
                    output += MainRuntime.ValueMap.get(token);
                } else if (token.equals("+")) {
                    // 运算符
                    Object left = output.substring(0, output.length() - 1);
                    String rightToken = tokens[i + 1];
                    Object right;
                    if (rightToken.startsWith("\"")) {
                        // 字符串类型
                        right = rightToken.substring(1, rightToken.length() - 1);
                    } else {
                        // 整数类型
                        right = Integer.parseInt(rightToken);
                    }
                    output = left.toString() + right.toString() + " ";
                    i += 2;
                } else {
                    // 整数类型
                    output += token;
                }
                if (i < tokens.length - 1) {
                    output += " ";
                }
            }
            return output;
        }
        catch (Exception exception)
        {
            MainRuntime.sendSyntaxError(exception.getMessage() , line);
            return null;
        }
    }
}