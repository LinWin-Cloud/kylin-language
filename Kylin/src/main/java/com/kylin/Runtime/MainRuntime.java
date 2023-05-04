package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;
import com.kylin.Exception.SyntaxError;
import com.kylin.Main;
import program.value.Value;

import javax.script.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MainRuntime {

    public static HashMap<String,String> function = new HashMap<>();
    //public static HashMap<String,Function> execFunctionHashMap = new HashMap<>();
    public static HashMap<String , Value> ValueMap = new HashMap<>();
    public static int codeLine = 0;

    public static void run(){
        int size = Main.code.size();
        for (codeLine = 0 ; codeLine < size ; codeLine++) {
            String source_code = Main.code.get(codeLine);
            try {
                MainRuntime.exec(source_code,"");
            }catch (Exception exception){
                MainRuntime.sendRuntimeError(exception.getMessage(),codeLine);
            }
        }
    }
    public static void exec(String source_code,String new_var) throws Exception {
        String[] words = source_code.split(" ");
        if (words == null) {
            return;
        }
        if (words[0].startsWith("//")) {
            return;
        }
        if (words[0].equals("int")) {
            try {
                String name = words[1];
                String value = source_code.substring(source_code.indexOf("=")+1).trim();
                int IntValue = Integer.parseInt(value);
                Value var_int = new Value();
                var_int.setContent(IntValue);
                var_int.setName(new_var+name);

                MainRuntime.ValueMap.put(name,var_int);
            }
            catch (Exception exception) {
                sendRuntimeError("Define integer numeric errors",MainRuntime.codeLine);
            }
        }
    }
    public static void sendSyntaxError(String message,int line) {
        SyntaxError syntaxError = new SyntaxError();
        syntaxError.setFile(Main.resource.getAbsolutePath());
        syntaxError.setMessage(message);
        syntaxError.setLine(line);
        syntaxError.setTime();
        System.out.println(syntaxError.getError());
        System.exit(0);
    }
    public static void sendRuntimeError(String message,int line) {
        RuntimeError runtimeError = new RuntimeError();
        runtimeError.setFile(Main.resource.getAbsolutePath());
        runtimeError.setLine(line);
        runtimeError.setTime();
        runtimeError.setMessage(message);
        System.out.println(runtimeError.getError());
    }
}
