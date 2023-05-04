package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;
import com.kylin.Exception.SyntaxError;
import com.kylin.Main;

import javax.script.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MainRuntime {

    public static HashMap<String,String> function = new HashMap<>();
    public static HashMap<String,Function> execFunctionHashMap = new HashMap<>();
    public static int codeLine = 0;

    public static void run(){
        int size = Main.code.size();
        for (codeLine = 0 ; codeLine < size ; codeLine++) {
            String source_code = Main.code.get(codeLine);
            try {
                MainRuntime.exec(source_code,size);
            }catch (Exception exception){
                MainRuntime.sendRuntimeError(exception.getMessage(),codeLine);
            }
        }
    }
    public static void exec(String source_code,int size) throws Exception {
        String[] words = source_code.split(" ");
        if (words[0].startsWith("//")) {
            return;
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
