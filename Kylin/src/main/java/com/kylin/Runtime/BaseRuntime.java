package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;

public class BaseRuntime {
    public static void exec(String code,int line) {
        if (code.startsWith("out")) {
            try {
                //System.out.println(code);
                String PrintContent = code.substring(code.indexOf("(")+1,code.lastIndexOf(")"));
                Value value = MainRuntime.value.get(PrintContent.trim());
                if (value == null) {
                    System.out.println(Expression.getExpression(PrintContent,line));
                }
                else {
                    System.out.println(value.value);
                }
            }
            catch (Exception exception) {
                MainRuntime.sendSyntaxError("Syntax Error",line);
            }
        }
        else {
            MainRuntime.sendRuntimeError("Unknown code: "+code,line);
        }
    }
}
