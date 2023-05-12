package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;
import com.kylin.Exception.SyntaxError;
import com.kylin.Main;
import program.value.Value;
import program.vm.BaseRuntime;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainRuntime {

    public HashMap<String,String> function = new HashMap<>();
    //public static HashMap<String,Function> execFunctionHashMap = new HashMap<>();
    public HashMap<String, CopyOnWriteArrayList> ListMap = new HashMap<>();
    public HashMap<String, String> ImportantCharset = new HashMap<>();
    public HashMap<String , Value> ValueMap = new HashMap<>();
    public String name;
    public ArrayList<String> code = new ArrayList<>();
    public int codeLine = 0;

    public void run(){
        int size = this.code.size();
        for (codeLine = 0 ; codeLine < size ; codeLine++) {
            String source_code = this.code.get(codeLine).trim();
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
        String e = this.ImportantCharset.get(words[0]);

        if (words[0].startsWith("//")) {
            return;
        }
        if (words[0].equals("#defined")) {
            try
            {
                String key = words[1];
                String value = words[2];
                this.ImportantCharset.put(value,key);
                return;
            }
            catch (Exception exception){
                MainRuntime.sendSyntaxError(exception.getMessage() ,codeLine);
            }
        }
        if (words[0].equals("var") || (e != null && e.equals("var"))) {
            try {
                String name = words[1];
                String value = source_code.substring(source_code.indexOf("=")+1).trim();
                //System.out.println(value);
                if (value.startsWith("[") && value.endsWith("]"))
                {
                    ListExpression listExpression = new ListExpression();
                    listExpression.def_list_expression(name, value.substring(1, value.length() -1) , codeLine , this);
                }
                else{
                    value = Expression.getExString(value , codeLine , this);
                }
                Value v = new Value();
                v.setName(name);
                v.setContent(value);
                v.setPublic(true);
                ValueMap.put(name,v);
                //System.out.println(name+" "+value);
            }
            catch (Exception exception) {
                sendRuntimeError("Define integer numeric errors",codeLine);
            }
        }
        if (words[0].equals("javascript:"))
        {
            for (int i = codeLine + 1; Main.code.)
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
