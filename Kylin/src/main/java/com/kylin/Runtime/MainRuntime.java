package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;
import com.kylin.Exception.SyntaxError;
import com.kylin.Main;
import program.value.Value;
import program.vm.BaseRuntime;


import java.util.ArrayList;
import java.util.HashMap;

public class MainRuntime {

    public HashMap<String,String> function = new HashMap<>();
    //public static HashMap<String,Function> execFunctionHashMap = new HashMap<>();
    public HashMap<String , Value> ValueMap = new HashMap<>();
    public String name;
    public static int codeLine = 0;

    public void run(){
        int size = Main.code.size();
        for (codeLine = 0 ; codeLine < size ; codeLine++) {
            String source_code = Main.code.get(codeLine).trim();
            if (source_code.equals("")) {
                continue;
            }
            try {
                exec(source_code,"");
            }catch (Exception exception){
                MainRuntime.sendRuntimeError(exception.getMessage(),codeLine);
            }
        }
    }
    public void exec(String source_code,String new_var) throws Exception {
        String[] words = source_code.split(" ");
        if (words[0].startsWith("//")) {
            return;
        }
        if (words[0].equals("var")) {
            try {
                String name = words[1];
                String value = source_code.substring(source_code.indexOf("=")+1).trim();
                //System.out.println(value);
                if (value.startsWith("[") && value.endsWith("]"))
                {

                }
                else{
                    value = Expression.getExString(value , MainRuntime.codeLine , this);
                }
                Value v = new Value();
                v.setName(name);
                v.setContent(value);
                v.setPublic(true);
                ValueMap.put(name,v);
                System.out.println(name+" "+value);
            }
            catch (Exception exception) {
                sendRuntimeError("Define integer numeric errors",MainRuntime.codeLine);
            }
        }
        else {
            BaseRuntime baseRuntime = new BaseRuntime();
            baseRuntime.run(source_code , codeLine , this);
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
