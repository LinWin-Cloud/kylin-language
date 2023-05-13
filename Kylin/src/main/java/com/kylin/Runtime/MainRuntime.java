package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;
import com.kylin.Exception.SyntaxError;
import com.kylin.Main;
import program.value.ExecFunction;
import program.value.Value;
import program.vm.BaseLib;
import program.vm.BaseRuntime;


import javax.script.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainRuntime {

    public HashMap<String,String> function = new HashMap<>();
    public HashMap<String,ExecFunction> execFunctionHashMap = new HashMap<>();
    public HashMap<String, CopyOnWriteArrayList> ListMap = new HashMap<>();
    public HashMap<String, String> ImportantCharset = new HashMap<>();
    public HashMap<String , Value> ValueMap = new HashMap<>();
    public String name;
    public ArrayList<String> code = new ArrayList<>();
    public int codeLine = 0;
    public boolean isFunction = false;
    public String result;

    public void run(){
        int size = this.code.size();
        for (codeLine = 0 ; codeLine < size ; codeLine++) {
            String source_code = this.code.get(codeLine).trim();
            //System.out.println(name+" "+source_code);
            if (source_code.equals("")) {
                continue;
            }
            try {
                this.exec(source_code,"");
            }catch (Exception exception){
                MainRuntime.sendRuntimeError(exception.getMessage(),codeLine);
            }
        }
    }
    public String exec(String source_code,String new_var) throws Exception {
        String[] words = source_code.split(" ");
        String e = this.ImportantCharset.get(words[0].trim());
        //System.out.println(this.ImportantCharset);

        if (words[0].startsWith("//")) {
            return "";
        }
        if (words[0].equals("#defined") || (e != null && e.equals("#defined"))) {
            try
            {
                String key = words[1];
                String value = words[2];
                this.ImportantCharset.put(value,key);
                return "";
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
                return "";
            }
            catch (Exception exception) {
                sendRuntimeError("Define integer numeric errors",codeLine);
            }
        }
        if (words[0].equals("javascript:"))
        {
            BaseLib.javascript(this.code , codeLine,this);
            return "";
        }
        if (source_code.startsWith("#head")) {
            try {
                String head = source_code.substring(5).trim();
                if (head.startsWith("<") && head.endsWith(">"))
                {
                    String input = head.substring(1,  head.length() - 1);
                    input = input.replace("{head}","../head");
                    File file = new File(input);
                    if (!file.isFile() || !file.canRead()) {
                        MainRuntime.sendRuntimeError("Can not open: "+file.getAbsolutePath() , codeLine);
                        System.exit(1);
                    }
                    else {
                        FileReader fileReader = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        while (true) {
                            String line = bufferedReader.readLine();
                            if (line == null){
                                break;
                            }
                            if (line.startsWith("#defined")) {
                                try
                                {
                                    String key = line.split(" ")[1];
                                    String value = line.split(" ")[2];
                                    this.ImportantCharset.put(value,key);
                                }
                                catch (Exception exception){
                                    MainRuntime.sendSyntaxError(exception.getMessage() ,codeLine);
                                }
                            }
                        }
                        bufferedReader.close();
                        fileReader.close();
                        return "";
                    }
                }
                else {
                    MainRuntime.sendSyntaxError(source_code , codeLine);
                }
            }
            catch (Exception exception){
                MainRuntime.sendSyntaxError(exception.getMessage() , codeLine);
            }
        }
        if (words[0].equals("func") || (e != null && e.equals("func")))
        {
            try {
                String regex = "\\s+|(?<=\\()\".*\"(?=\\))|(?<=\\()\".*\",\\s*\".*\"(?=\\))|\\s*->\\s*";
                String[] splitFunctionTitle = source_code.split(regex);
                String functionName_content = splitFunctionTitle[1];
                boolean isPublic = splitFunctionTitle[3].toLowerCase().equals("public");

                String[] input = functionName_content.substring(functionName_content.indexOf("(")+1,functionName_content.lastIndexOf(")")).split(",");
                String FunctionName = functionName_content.substring(0,functionName_content.indexOf("(")).replace(" ","");

                ExecFunction execFunction = new ExecFunction();
                execFunction.setMainRuntime(this);
                execFunction.setPublic(isPublic);
                execFunction.inputList = input;
                execFunction.setName(FunctionName);

                List<String> codeList = new ArrayList<>();
                for (int i = codeLine + 1 ; i < this.code.size() ;i++)
                {
                    String code = this.code.get(i).trim();
                    if (code.equals("end_func")) {
                        codeLine = i;
                        break;
                    }
                    codeList.add(code);
                }
                execFunction.code = codeList;
                this.execFunctionHashMap.put(FunctionName , execFunction);
                return "";
            }
            catch (Exception exception){
                MainRuntime.sendSyntaxError(source_code,codeLine);
            }
        }
        else {
            try {
                String UseFunction = source_code.substring(0,source_code.indexOf("(")).trim();
                if (this.execFunctionHashMap.containsKey(UseFunction))
                {
                    try {
                        String getInput = source_code.substring(source_code.indexOf("(")+1,source_code.lastIndexOf(")"));
                        ListExpression listExpression = new ListExpression();
                        String[] inputContent = listExpression.get_list_expression(getInput,codeLine , this);
                        ExecFunction execFunction = this.execFunctionHashMap.get(UseFunction);

                        //System.out.println(Arrays.toString(execFunction.inputList));
                        //System.out.println(Arrays.toString(inputContent));
                        for (int i = 0 ; i < execFunction.inputList.length ;i++)
                        {
                            execFunction.input.put(execFunction.inputList[i] , inputContent[i]);
                        }
                        execFunction.RunFunction();
                    }catch (Exception exception) {
                        exception.printStackTrace();
                        MainRuntime.sendSyntaxError("ERR: "+exception.getMessage(),codeLine);
                    }
                }
            }
            catch (Exception exception) {
                BaseRuntime baseRuntime = new BaseRuntime();
                baseRuntime.run(source_code , codeLine , this);
            }
        }
        return "";
    }
    public String getResult() {
        if (isFunction) {
            return this.result;
        }
        else {
            return "";
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
