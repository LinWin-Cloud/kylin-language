package Program;

import main.baseFunction;

import java.util.*;
import java.util.Map;

import KylinException.KylinRuntimeException;
public class KylinRuntime {
    public ArrayList<String> code;
    public boolean isError = false;
    public Map<String , KylinValue> ValueMap = new HashMap<>();                 // 储存本运行环境的变量
    public Map<String , KylinFunction> FunctionMap = new HashMap<>();           // 存储本运行环境的函数
    public Map<String , KylinFunction> ExceptionMap = new HashMap<>();          // 储存本运行环境的异常处理函数
    public boolean isFunction = false;                                          // 本运行环境是否是 函数
    private String result = "";                                 
    public KylinRuntime PublicRuntime;                                          // 本运行环境的公共运行环境，如果是局部运行环境，那么这个是存在的，如果不是就是 null.
    public int codeLine = 0;
    public String getResult() {
        return this.result;
    }
    public Map<String , String> defined_keyword = new HashMap<>();              // 定义的关键字hashmap
    public Map<String , String> defined_func = new HashMap<>();                 // 定义的函数关键字，例如 out("hello world") 可以定义为 print("hello world")
    public String name;                                                         // 本运行环境名字
    private boolean isIf = false;                                               // 上一次是否存在if语句，如果存在，那么允许else语句存在
    private boolean IF_OK = false;                                              // if语句是否结束

    public KylinRuntime(String name) {
        this.name = name;
    }

    public void exec(String code, int i) throws Exception {
        //通过空格分割字符串
        String[] words = code.trim().split(" ");
        if (code.equals("")) {
            return;
        }
        String keyword = this.defined_keyword.get(words[0]);
        // 这个keyword对应一个hashmap中的value , 例如中文编程下 "变量 a = 1"，那么这个 "变量" 就会对应一个值 "var" ，同样可以处理语句
        if (keyword == null) {
            keyword = "";
        }
        if (code.startsWith("//")) {
            // 这条是处理注释，是注释的直接过去
            return;
        }
        if (words[0].equals("var") || keyword.equals("var")) {
            //这个是定义变量的语句
            String name = words[1];
            String content = code.substring(code.indexOf("=")+1).trim();
            KylinValue kylinValue = new KylinValue();
            kylinValue.setContent(content ,this);
            kylinValue.setName(name);
            this.ValueMap.put(name , kylinValue);
            return;
        }
        if (isFunction && (words[0].equals("return") || keyword.equals("return"))) {
            this.result = new KylinExpression().getExpression(code.substring(code.indexOf("return ")+"return ".length()).trim(), this);
            return;
        }
        else if (words[0].equals("#defined") || keyword.equals("#defined")) {
            // 定义关键字是什么
            String key = words[1];
            String value = words[2];
            this.defined_keyword.put(value , key);
            return;
        }
        else if (words[0].equals("#func") || keyword.equals("#func"))
        {
            String key = words[1];
            String value = words[2];
            this.defined_func.put(value , key);
            return;
        }
        else if (words[0].equals("func") || words[0].equals("f") || keyword.equals("func") || keyword.equals("f")) {
            /**
             * func func_name (a ,b) public
             *      return <a + b>
             * end_func
             * 
             * f func_name() 
             *      return "hello world"
             * e_f
             */
                String name = code.substring(code.indexOf(" ")+1,code.indexOf("(")).trim();
                String inputContent = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).replace(" ","");
                boolean isPublic;

                if (words[0].equals("func") || keyword.equals("func")) {
                    isPublic = baseFunction.isPublic(code.substring(code.lastIndexOf(")")+1).trim());
                }else{
                    isPublic = false;
                }

                KylinFunction kylinFunction = new KylinFunction(name);
                kylinFunction.isPublic = isPublic;
                kylinFunction.setInput(inputContent.split(","));

                ArrayList<String> functionCode = new ArrayList<>();
                if (words[0].equals("func") || keyword.equals("func")) {
                    for (int j = i+1 ; j < this.code.size() ;j++) {
                        String line = this.code.get(j).trim();
                        if ((line.equals("end_func"))) {
                            this.codeLine = j;
                            break;
                        }
                        if (line.equals("")) {
                            continue;
                        }
                        functionCode.add(line);
                    }
                }else{
                    for (int j = i+1 ; j < this.code.size() ;j++) {
                        String line = this.code.get(j).trim();
                        if ((line.equals("e_f"))) {
                            this.codeLine = j;
                            break;
                        }
                        if (line.equals("")) {
                            continue;
                        }
                        functionCode.add(line);
                    }
                }
                kylinFunction.kylinRuntime.code = functionCode;
                kylinFunction.kylinRuntime.PublicRuntime = this;
                this.FunctionMap.put(name , kylinFunction);
                return;
        }
        else if (words[0].equals("if") || keyword.equals("if")) {
            this.isIf = true;
            String[] splitArray = code.split("(?=\\s[a-zA-Z]+\\()");

            String IF = splitArray[0].trim();
            String func = splitArray[1].trim();

            String IF_DO = IF.substring(IF.indexOf("(")+1,IF.length()-1);
            boolean isTrue = new KylinBoolean().isBool(IF_DO,this);
            if (isTrue) {
                this.IF_OK = true;
                //System.out.println(func);
                //System.out.println(this.FunctionMap.keySet());
                new KylinExpression().getExpression(func,this);
            }
            return;
        }
        else if (words[0].equals("else") && this.isIf && !this.IF_OK) {
            this.isIf = false;
            String func = code.substring(code.indexOf(" ")+1);
            new KylinExpression().getExpression(func,this);
        }
        else if (words[0].equals("err") || keyword.equals("err")) {
            //定义异常处理函数
            String name = code.substring(code.indexOf(" ")+1,code.indexOf("(")).trim();
            String inputContent = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).replace(" ","");
            boolean isPublic = false;

            KylinFunction kylinFunction = new KylinFunction(name);
            kylinFunction.isPublic = isPublic;

            ArrayList<String> functionCode = new ArrayList<>();
            for (int j = i+1 ; j < this.code.size() ;j++) {
                String line = this.code.get(j).trim();
                if ((line.equals("e_err"))) {
                    this.codeLine = j;
                    break;
                }
                if (line.equals("")) {
                    continue;
                }
                functionCode.add(line);
            }
            kylinFunction.kylinRuntime.code = functionCode;
            kylinFunction.kylinRuntime.PublicRuntime = this;
            kylinFunction.isException = true;
            kylinFunction.err_code = inputContent+".message";
            KylinValue kylinValue = new KylinValue();
            kylinValue.setName(inputContent+".message");
            kylinFunction.kylinRuntime.ValueMap.put(inputContent+".message",kylinValue);
            this.ExceptionMap.put(name , kylinFunction);
            return;
        }
        else if (words[0].equals("import")) {
            String lib = code.substring(code.indexOf("\"")+1,code.lastIndexOf("\""));
            ImportLib.lib_import(lib.trim(),this);
            return;
        }
        else if (code.startsWith("#include")) {
            main.baseFunction.include(code , this);
            return;
        }
        else if (isFunction(code)) {
            runFunction(code);
            return;
        }
        else if (KylinProgramBaseFunction.isProgramBaseFunction(code , this)) {
            KylinProgramBaseFunction.runProgramBaseFunction(code,this);
        }
        else {
            KylinRuntimeException kylinRuntimeException = new KylinRuntimeException("code error.",this.codeLine,true);
            kylinRuntimeException.PrintErrorMessage(this);
        }
    }
    public void run() throws Exception {
        for (this.codeLine = 0 ; this.codeLine < this.code.size() ;codeLine++) {
            this.exec(this.code.get(codeLine) , codeLine);
        }
    }
    public boolean isFunction(String code) {
        try {
            String function = code.substring(0,code.indexOf("(")).trim();
            String content = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
            return this.FunctionMap.containsKey(function);
        }catch (Exception exception) {
            //exception.printStackTrace();
            return false;
        }
    }
    public boolean runFunction(String code) throws Exception {
        String function = code.substring(0,code.indexOf("(")).trim();
        String content = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
        if (this.FunctionMap.containsKey(function)) {
            KylinFunction kylinFunction =this.FunctionMap.get(function);
            String[] split = content.split(",\\s*");
            //System.out.println(kylinFunction.input.length);
            //System.out.println(split.length);
            for (int i = 0 ; i < kylinFunction.input.length ;i++) {
                KylinValue kylinValue = new KylinValue();
                kylinValue.setName(kylinFunction.input[i]);
                kylinValue.setContent(split[i] , this);
                kylinFunction.kylinRuntime.ValueMap.put(kylinFunction.input[i] , kylinValue);
            }
            kylinFunction.kylinRuntime.run();
            return true;
        }else {
            return false;
        }
    }
}
