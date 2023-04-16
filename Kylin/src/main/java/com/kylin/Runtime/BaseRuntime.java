package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;

public class BaseRuntime {
    public static void exec(String code,int line) throws Exception {
        boolean b = isFunction(code);
        if (b) {
            return;
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
            catch (Exception exception1) {
                MainRuntime.sendSyntaxError("Syntax Error: "+exception1.getMessage(),line);
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
    public static boolean isFunction(String code) {
        try {
            //System.out.println(code);
            String name = code.substring(0,code.indexOf("(")).replace(" ","");
            //System.out.println(name);
            String input = code.substring(code.indexOf("(")+1,code.indexOf(")"));
            //System.out.println(input+";");
            Function function = MainRuntime.execFunctionHashMap.get(name);
            //System.out.println(MainRuntime.execFunctionHashMap.toString()+"    "+name+";");
            if (function != null) {
                function.exec(input);
                return true;
            }
            else {
                return false;
            }
        }catch (Exception exception) {
            //exception.printStackTrace();
            return false;
        }
    }
}
