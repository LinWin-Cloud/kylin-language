package com.kylin.Runtime;

public class Expression 
{
    public static String getExString(String content , int line,MainRuntime mainRuntime)
    {
        try
        {
            /**
             * Split all the token.
             */
            StringBuffer stringBuffer = new StringBuffer("");
            String[] tokens = content.split(" ");
            boolean isStr = false;
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i].trim();
                //System.out.println(token);
                if (isStr && token.endsWith("\"")) {
                    isStr = false;
                    stringBuffer.append(token, 0, token.length() - 1);
                    continue;
                }
                else if(token.startsWith("\"")) {
                    isStr = true;
                    stringBuffer.append(token.substring(1));
                    stringBuffer.append(" ");
                    continue;
                }
                else if (mainRuntime.ValueMap.containsKey(token)) {
                    stringBuffer.append(mainRuntime.ValueMap.get(token).getContent());
                    continue;
                }
                else {
                    stringBuffer.append(token);
                    continue;
                }
            }
            return stringBuffer.toString();
        }
        catch (Exception exception)
        {
            MainRuntime.sendSyntaxError(exception.getMessage() , line);
            return null;
        }
    }
}