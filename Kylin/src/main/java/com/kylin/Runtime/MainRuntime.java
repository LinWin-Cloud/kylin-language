package com.kylin.Runtime;

import com.kylin.Exception.RuntimeError;
import com.kylin.Exception.SyntaxError;
import com.kylin.Main;
import program.value.ExecFunction;
import program.value.Value;
import program.vm.BaseLib;
import program.vm.BaseRuntime;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainRuntime {

    public Hashtable<String,ExecFunction> execFunctionHashMap = new Hashtable<>();
    public HashMap<String, CopyOnWriteArrayList> ListMap = new HashMap<>();
    public HashMap<String, String> ImportantCharset = new HashMap<>();
    public HashMap<String , Value> ValueMap = new HashMap<>();
    public MainRuntime PublicRuntime;
    public String name;
    public ArrayList<String> code = new ArrayList<>();
    public int codeLine = 0;
    public boolean isFunction = false;
    public String result;
    public String exceptionCode;
    public String exceptionMessage;
    public boolean isException = false;
    private boolean inTry = false;
    public ArrayList<String> CatchCode;

    public MainRuntime(String name)
    {
    }
    public void run() throws Exception{
        int size = this.code.size();
        for (codeLine = 0 ; codeLine < size ; codeLine++) {
            String source_code = this.code.get(codeLine).trim();
            if (source_code.equals("")) {
                continue;
            }
            if (isFunction && source_code.startsWith("return "))
            {
                try{
                    this.result = Expression.getExString(source_code.substring(7) , codeLine , this);
                    continue;
                }catch (Exception exception){
                    MainRuntime.sendSyntaxError(source_code,codeLine);
                }
            }
            this.exec(source_code,"");
        }
    }
    public void exec(String source_code,String new_var) throws Exception {
        String[] words = source_code.split(" ");
        String e = ImportantCharset.get(words[0].trim());

        if (words[0].startsWith("//")) {
            return;
        }
        if (words[0].equals("#defined") || (e != null && e.equals("#defined"))) {
            try
            {
                String key = words[1];
                String value = words[2];
                ImportantCharset.put(value,key);
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
                return;
            }
            catch (Exception exception) {
                sendRuntimeError("Define integer numeric errors",codeLine);
            }
        }
        if (words[0].equals("javascript:"))
        {
            BaseLib.javascript(code , codeLine,this);
            return;
        }
        if (words[0].equals("import")) {
            ImportFunction importFunction = new ImportFunction();
            importFunction.Import(this , words[1]);
            return;
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
                        return;
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
                String functionName_content = splitFunctionTitle[1].replace(" ","");
                boolean isPublic = splitFunctionTitle[3].toLowerCase().equals("public");

                String[] input = functionName_content.substring(functionName_content.indexOf("(")+1,functionName_content.lastIndexOf(")")).split(",");
                String FunctionName = functionName_content.substring(0,functionName_content.indexOf("(")).replace(" ","");

                ExecFunction execFunction = new ExecFunction();
                execFunction.setMainRuntime(this);
                execFunction.setPublic(isPublic);
                execFunction.inputList = input;
                execFunction.setName(FunctionName);
                execFunction.setMainRuntime(this);
                execFunction.lastRuntime = this.name;
                execFunction.setMainRuntime(this);

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
                return;
            }
            catch (Exception exception){
                MainRuntime.sendSyntaxError(source_code,codeLine);
            }
        }
        if (source_code.equals("try")) {
            try
            {
                //System.out.println(this.execFunctionHashMap.keySet());
                this.inTry = true;
                ArrayList<String> exceptionCode = new ArrayList<>();
                ArrayList<String> CatchCode = new ArrayList<>();
                MainRuntime exceptionRuntime = new MainRuntime(String.valueOf(new Random().nextLong()));
                exceptionRuntime.isException = true;
                String catchValue = null;
                for (int i = this.codeLine + 1 ; i < this.code.size() ;i++)
                {
                    //System.out.println(this.code.get(i));
                    String TryCatchCode = this.code.get(i);
                    if (TryCatchCode.startsWith("catch")) {
                        catchValue = TryCatchCode.substring(
                                TryCatchCode.indexOf("(")+1,
                                TryCatchCode.lastIndexOf(")")).trim();
                        Value value = new Value();
                        value.setType("string");
                        value.setPublic(false);
                        value.setName(catchValue+".message");
                        value.setContent("kylin.runtime.exception");
                        exceptionRuntime.ValueMap.put(value.getName() , value);
                        codeLine = i + 1;
                        break;
                    }
                    exceptionCode.add(TryCatchCode);
                }
                for (int i = codeLine ;i < this.code.size() ;i++)
                {
                    String catchCode = code.get(i);
                    if (catchCode.equals("end_catch")) {
                        codeLine = i;
                        break;
                    }
                    CatchCode.add(catchCode);
                }
                exceptionRuntime.code = exceptionCode;
                exceptionRuntime.ValueMap.putAll(this.ValueMap);
                exceptionRuntime.execFunctionHashMap.putAll(this.execFunctionHashMap);
                exceptionRuntime.CatchCode = CatchCode;
                try {
                    exceptionRuntime.run();
                }
                catch (Exception exception) {
                    exceptionRuntime.ValueMap.get(catchValue+".message").setContent(exception.getMessage());
                    exceptionRuntime.code = CatchCode;
                    exceptionRuntime.run();
                }
                return;
            }catch (Exception exception) {
                MainRuntime.sendSyntaxError(exception.getMessage() , codeLine);
            }
        }
        else {
            if (!isUseFunction(source_code)) {
                BaseRuntime baseRuntime = new BaseRuntime();
                baseRuntime.run(source_code, codeLine, this);
            }
            return;
        }
        return;
    }
    private boolean isUseFunction(String source_code) {
        try {
            String UseFunction = source_code.substring(0,source_code.indexOf("(")).trim();
            //System.out.println(UseFunction+";"+this.execFunctionHashMap.containsKey(UseFunction));
            if (this.execFunctionHashMap.containsKey(UseFunction))
            {
                FuncRun(source_code , UseFunction);
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception exception) {
            return false;
        }
    }
    private void FuncRun(String source_code , String UseFunction) throws Exception {
        String getInput = source_code.substring(source_code.indexOf("(")+1,source_code.lastIndexOf(")"));
        ListExpression listExpression = new ListExpression();
        String[] inputContent = listExpression.get_list_expression(getInput,codeLine , this);
        ExecFunction execFunction = this.execFunctionHashMap.get(UseFunction);

        for (int i = 0 ; i < execFunction.inputList.length ;i++)
        {
            Value value = new Value();
            value.setPublic(false);
            value.setName(execFunction.inputList[i]);
            value.setContent(inputContent[i]);
            execFunction.input.put(execFunction.inputList[i] , value);
        }
        //System.out.println("hello world");
        execFunction.RunFunction();
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
class TryCatch {
    public ArrayList<String> catchCode = new ArrayList<>();
    public ArrayList<ExecFunction> codeList = new ArrayList<>();
}