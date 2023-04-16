package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;

public class BaseRuntime {
    public static void exec(String code,int line) throws Exception {

        try {
            Function function = MainRuntime.execFunctionHashMap.get(code.substring(0,code.indexOf("(")));
            if (function != null) {
                String inCode= code.substring(code.indexOf("(")+1,code.lastIndexOf(")"));
                function.exec(inCode);
                return;
            }
            return;
        }catch (Exception exception){
        }

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
                MainRuntime.sendSyntaxError("Syntax Error: "+exception,line);
            }
            return;
        }
        if (code.equals("")) {
            return;
        }
        else {
            MainRuntime.sendRuntimeError("Unknown code: "+code,line);
            return;
        }
    }
}
