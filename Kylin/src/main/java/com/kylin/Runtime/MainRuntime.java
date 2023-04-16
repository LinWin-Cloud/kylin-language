package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;
import com.kylin.Exception.SyntaxError;
import com.kylin.Main;

import javax.script.*;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

public class MainRuntime {

    public static HashMap<String,String> function = new HashMap<>();
    public static HashMap<String,Function> execFunctionHashMap = new HashMap<>();
    public static int codeLine = 0;
    public static ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    public static ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("js");

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
        if (words[0].startsWith("func")) {
            try {
                int start , end;
                String FunctionName = source_code.substring(5,source_code.indexOf("(")).trim();
                String FunctionContent = source_code.substring(source_code.indexOf("(")+1,source_code.lastIndexOf(")"));
                String[] InputValue = FunctionContent.split(",");

                ArrayList<String> FunctionCode = new ArrayList<>();

                //System.out.println("function start: "+codeLine);
                start = codeLine;
                for (int i = codeLine ; codeLine < size ; i++) {
                    String line = Main.code.get(i).trim();

                    if (line.equals("end")) {
                        codeLine = i;
                        //System.out.println("Function end:" + codeLine);
                        break;
                    }
                    FunctionCode.add(line);
                }
                end = codeLine;
                Function func = new Function();
                func.FunctionCode = FunctionCode;
                func.FunctionName = FunctionName;
                func.start = start;
                func.end = end;
                for (String i : InputValue){
                    Value v = new Value();
                    v.name = "$"+i.trim();
                    func.FunctionValue.add(v);
                }
                MainRuntime.execFunctionHashMap.put(func.FunctionName,func);
            }catch (Exception exception){
                sendSyntaxError("Defined function error.",codeLine+1);
            }
            return;
        }
        if (words[0].startsWith("let")) {
            try {
                String name = words[1];
                String value = source_code.substring(source_code.indexOf("=")+1).trim();
                Value NewValue = new Value();
                NewValue.name = "$"+name;
                NewValue.value = value;
                //System.out.println(name+" "+value+";");
                MainRuntime.value.put(NewValue.name,NewValue);
            }
            catch (Exception exception){
                sendSyntaxError("Defined variable error: "+exception.getMessage(),codeLine+1);
            }
            return;
        }
        if (words[0].startsWith("var")) {
            try {
                scriptEngine.eval(source_code);
            }
            catch (Exception exception){
                sendSyntaxError("Defined variable error: "+exception.getMessage(),codeLine+1);
            }
            return;
        }
        if (words[0].startsWith("try")){
            ArrayList<String> TryCode = new ArrayList<>();
            ArrayList<String> CatchCode = new ArrayList<>();
            try {
                String CatchValue = null;
                TryCodeFor:
                for (int i = codeLine+1 ; i < Main.code.size() ; i++) {
                    String line = Main.code.get(i);
                    if (line.startsWith("error")) {
                        CatchValue = line.substring(line.indexOf("(")+1,line.lastIndexOf(")"));
                        codeLine = i;

                        CatchCodeFor:
                        for (int j = codeLine+1 ; j < size ; j++) {
                            String Catch = Main.code.get(j);
                            if (Catch.equals("end")) {
                                codeLine = j + 1;
                                break CatchCodeFor;
                            }else {
                                CatchCode.add(Catch);
                            }
                        }
                        break TryCodeFor;
                    }
                    TryCode.add(line.trim());
                }
                ExceptionCatch exceptionCatch = new ExceptionCatch();
                exceptionCatch.CatchCode = CatchCode;
                exceptionCatch.TryCode = TryCode;
                exceptionCatch.ExceptionValue = CatchValue;
                Main.ExceptionCode.add(exceptionCatch);

                for (String i : exceptionCatch.TryCode) {
                    try {
                        MainRuntime.exec(i,size);
                    }
                    catch (Exception exception){
                        String[] KeyCode = {".message",".line",".path"};
                        String[] ValueCode = {exception.getMessage(),String.valueOf(codeLine),Main.resource.getAbsolutePath()};

                        for (int a = 0 ; a < KeyCode.length ; a++) {
                            Value v = new Value();
                            v.name = exceptionCatch.ExceptionValue + KeyCode;
                            v.value = exceptionCatch.ExceptionValue + ValueCode;

                            MainRuntime.value.put(v.name,v);
                        }
                        for (String j : CatchCode) {
                            MainRuntime.exec(j,size);
                        }
                        break;
                    }
                }
                return;
            }catch (Exception exception){
                MainRuntime.sendRuntimeError(exception.getMessage(),codeLine);
            }
            return;
        }
        else {
            BaseRuntime.exec(source_code,codeLine+1);
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
