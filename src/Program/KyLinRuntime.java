package Program;

import Function.TypeOf;
import main.PathLoader;
import main.baseFunction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import KylinException.KylinRuntimeException;
import main.mainApp;

public class KyLinRuntime {
    public ArrayList<String> code;
    public boolean isError = false;
    public ConcurrentHashMap<String , KyLinValue> ValueMap = new ConcurrentHashMap<>();                 // 储存本运行环境的变量
    public Map<String , KyLinFunction> FunctionMap = new HashMap<>();           // 存储本运行环境的函数
    public Map<String , KyLinFunction> ExceptionMap = new HashMap<>();          // 储存本运行环境的异常处理函数
    public boolean isFunction = false;                                          // 本运行环境是否是 函数
    private String result = "";                                 
    public KyLinRuntime PublicRuntime;                                          // 本运行环境的公共运行环境，如果是局部运行环境，那么这个是存在的，如果不是就是 null.
    public int codeLine = 0;
    public String getResult() {
        return this.result;
    }
    public Map<String , String> defined_keyword = new HashMap<>();              // 定义的关键字hashmap
    public Map<String , String> defined_func = new HashMap<>();                 // 定义的函数关键字，例如 out("hello world") 可以定义为 print("hello world"),kylin3.0废除该标准
    public String name;                                                         // 本运行环境名字
    private boolean isIf = false;                                               // 上一次是否存在if语句，如果存在，那么允许else语句存在
    private boolean IF_OK = false;                                              // if语句是否结束
    public Map<String , KyLinClass> classMap = new HashMap<>();                 // 本运行环境下的 类 储存
    public boolean OnErrorExit = true;
    public KyLinRuntime(String name) {
        this.name = name;
    }
    public boolean isClass = false;


    public void exec(String code, int i) throws Exception
    {
        String[] words = code.trim().split(" ");
        if (code.equals(""))
        {
            return;
        }
        String keyword = this.defined_keyword.get(words[0]);
        // 这个keyword对应一个hashmap中的value , 例如中文编程下 "变量 a = 1"，那么这个 "变量" 就会对应一个值 "var" ，同样可以处理语句
        if (keyword == null)
        {
            keyword = "";
        }
        if (code.startsWith("//"))
        {
            // 这条是处理注释，是注释的直接过去
            return;
        }
        if (words[0].equals("var") || keyword.equals("var"))
        {
            this.new_value(code,true);
            return;
        }
        if (isFunction && (words[0].equals("return") || keyword.equals("return")))
        {
            this.result = new KyLinExpression().getExpression(code.substring(code.indexOf("return ")+"return ".length()).trim(), this);
            return;
        }
        /**
         *  在 kylin 编程语言 3.0 中删除对 #func 和 #defined 的支持，因为这些特性用处不大而且极大地拖慢了 kylin vm的运行速度.
         *         else if (words[0].equals("#func") || keyword.equals("#func"))
         *         {
         *             String key = words[1];
         *             String value = words[2];
         *             this.defined_func.put(value , key);
         *             return;
         *         }
         */
        else if (words[0].equals("val") || keyword.equals("val")) {
            new KyLinVal().Val(code,this);
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

                if (words[0].equals("func") || keyword.equals("func"))
                {
                    isPublic = baseFunction.isPublic(code.substring(code.lastIndexOf(")")+1).trim());
                }else
                {
                    isPublic = false;
                }

                KyLinFunction kylinFunction = new KyLinFunction(name);
                kylinFunction.isPublic = isPublic;
                kylinFunction.setInput(inputContent.split(","));

                ArrayList<String> functionCode = new ArrayList<>();
                if (words[0].equals("func") || keyword.equals("func"))
                {
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
                    for (int j = i+1 ; j < this.code.size() ;j++)
                    {
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
        else if (words[0].equals("if") || keyword.equals("if"))
        {
            this.isIf = true;
            String[] splitArray = code.split("\\)\\s");

            String IF = splitArray[0].trim();
            String func = splitArray[1].trim();

            String IF_DO = IF.substring(IF.indexOf("(")+1);
            boolean isTrue = new KyLinBoolean().isBool(IF_DO,this);

            if (isTrue)
            {
                this.IF_OK = true;
                //System.out.println(func);
                //System.out.println(this.FunctionMap.keySet());
                new KyLinExpression().getExpression(func,this);
            }
            return;
        }
        else if (words[0].equals("else") && this.isIf && !this.IF_OK)
        {
            this.isIf = false;
            String func = code.substring(code.indexOf(" ")+1);
            new KyLinExpression().getExpression(func,this);
        }
        else if (words[0].equals("err") || keyword.equals("err"))
        {
            //定义异常处理函数
            String name = code.substring(code.indexOf(" ")+1,code.indexOf("(")).trim();
            String inputContent = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).replace(" ","");
            boolean isPublic = false;

            KyLinFunction kylinFunction = new KyLinFunction(name);
            kylinFunction.isPublic = isPublic;

            ArrayList<String> functionCode = new ArrayList<>();
            for (int j = i+1 ; j < this.code.size() ;j++)
            {
                String line = this.code.get(j).trim();
                if ((line.equals("e_err")))
                {
                    this.codeLine = j;
                    break;
                }
                if (line.equals(""))
                {
                    continue;
                }
                functionCode.add(line);
            }
            kylinFunction.kylinRuntime.code = functionCode;
            kylinFunction.kylinRuntime.PublicRuntime = this;
            kylinFunction.isException = true;
            kylinFunction.err_code = inputContent+".message";
            KyLinValue kylinValue = new KyLinValue();
            kylinValue.setName(inputContent+".message");
            kylinFunction.kylinRuntime.ValueMap.put(inputContent+".message",kylinValue);
            this.ExceptionMap.put(name , kylinFunction);
            return;
        }
        else if (words[0].equals("class") || keyword.equals("class"))
        {
            String name = code.substring(code.indexOf(" ")+1,code.indexOf(":")).trim();
            boolean isPublic = main.baseFunction.isPublic(code.substring(code.lastIndexOf(":")+1).trim());

            KyLinClass kyLinClass = new KyLinClass(name);
            kyLinClass.isPublic = isPublic;

            for (int j = i + 1 ; j < this.code.size() ; j++)
            {
                String line = this.code.get(j).trim();
                if (line.equals(""))
                {
                    continue;
                }
                if (line.equals("end_class"))
                {
                    this.codeLine = j;
                    break;
                }
                kyLinClass.code.add(line);
            }
            kyLinClass.init_class();
            this.classMap.put(name , kyLinClass);
            return;
        }
        else if (words[0].equals("import") || keyword.equals("import"))
        {
            String lib = new KyLinExpression().getExpression(code.substring(7) , this);
            String lib_path = PathLoader.getLibName(lib);
            ImportLib.lib_import(lib_path,this);
            return;
        }
        else if (code.startsWith("#include"))
        {
            main.baseFunction.include(code , this);
            return;
        }
        else if (isFunction(code))
        {
            runFunction(code);
            return;
        }
        else if (KyLinProgramBaseFunction.isProgramBaseFunction(code , this))
        {
            KyLinProgramBaseFunction.runProgramBaseFunction(code,this);
        }
        else {
            KylinRuntimeException kylinRuntimeException = new KylinRuntimeException("code error.",this.codeLine,OnErrorExit);
            kylinRuntimeException.PrintErrorMessage(this);
            //new KyLinExpression().getExpression(code,this);
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
        String function = code.substring(0, code.indexOf("(")).trim();
        String content = code.substring(code.indexOf("(") + 1, code.lastIndexOf(")")).trim();

        if (this.FunctionMap.containsKey(function)) {

            KyLinFunction kylinFunction = this.FunctionMap.get(function);
            String[] split = content.split(",(?![^(]*\\))");
            int inputLength = kylinFunction.input.length;

            kylinFunction.kylinRuntime.FunctionMap.putAll(this.FunctionMap);

            for (int i = 0; i < inputLength; i++) {
                String input = kylinFunction.input[i];
                KyLinValue kylinValue = new KyLinValue();
                kylinValue.setName(input);
                kylinValue.setContent(new KyLinExpression().getExpression(split[i] , this), this);
                kylinFunction.kylinRuntime.ValueMap.put(input,kylinValue);
            }
            kylinFunction.kylinRuntime.run();
            return true;
        }else if (this.PublicRuntime != null && this.PublicRuntime.FunctionMap.containsKey(function)) {
            KyLinFunction kylinFunction = this.PublicRuntime.FunctionMap.get(function);
            kylinFunction.kylinRuntime.PublicRuntime = this;
            String[] split = content.split(",(?![^(]*\\))");
            int inputLength = kylinFunction.input.length;
            for (int i = 0; i < inputLength; i++) {
                String input = kylinFunction.input[i];
                KyLinValue kylinValue = new KyLinValue();
                kylinValue.setName(input);
                kylinValue.setContent(new KyLinExpression().getExpression(split[i] , this), this);
                kylinFunction.kylinRuntime.ValueMap.put(input,kylinValue);
            }
            kylinFunction.kylinRuntime.run();
            return true;
        }
        else {
            return false;
        }
    }
    public boolean new_class(String code , String content , boolean isPublic , KyLinRuntime kyLinRuntime) throws Exception {
        if (content.replace(" ","").startsWith("new(") && content.endsWith(")"))
        {
            String name = code.substring(code.indexOf(" ")+1 , code.indexOf("=")).trim();
            String new_class = content.substring(content.indexOf("(")+1,content.lastIndexOf(")")).trim();
            if (kyLinRuntime.classMap.containsKey(new_class)) {
                KyLinValue value = new KyLinValue();
                value.setName(name);
                value.setContent(kyLinRuntime.classMap.get(new_class) , this);
                value.setType(new_class);
                value.setIs_public(isPublic);
                /**
                 * Init a new class.
                 */
                KyLinClass tmp = kyLinRuntime.classMap.get(new_class);
                KyLinClass kyLinClass = new KyLinClass(tmp.name);
                kyLinClass.code = tmp.code;
                kyLinClass.isPublic = tmp.isPublic;
                kyLinClass.name = tmp.name;
                kyLinClass.kyLinRuntime = tmp.kyLinRuntime;
                kyLinRuntime.isClass = true;

                for (KyLinFunction kyLinFunction : kyLinClass.kyLinRuntime.FunctionMap.values()) {
                    if (kyLinFunction.isPublic) {
                        String stringBuffer = value.getName() +
                                "." +
                                kyLinFunction.name;
                        this.FunctionMap.put(stringBuffer, kyLinFunction);
                    }
                }
                for (KyLinValue kyLinValue : kyLinClass.kyLinRuntime.ValueMap.values()) {
                    if (kyLinValue.isPublic()) {
                        String stringBuffer = value.getName() +
                                "." +
                                kyLinValue.getName();
                        this.ValueMap.put(stringBuffer, kyLinValue);
                    }
                }
                kyLinClass.run_init_();
            }else {
                throw new Exception("Can not init a new class: "+new_class);
            }
            return true;
        }
        return false;
    }
    public boolean new_list(String code , String content , boolean isPublic , KyLinRuntime kyLinRuntime) throws Exception {
        if (content.replace(" ","").startsWith("list(") && content.endsWith(")"))
        {
            String name = code.substring(code.indexOf(" ")+1 , code.indexOf("=")).trim();
            String new_list = content.substring(content.indexOf("(")+1,content.lastIndexOf(")")).trim();

            KyLinList kyLinList = new KyLinList();
            kyLinList.name = name;

            String[] tmp = new_list.split(",(?![^(]*\\))");
            ArrayList<KyLinValue> arrayList = new ArrayList<>();
            for (String i : tmp)
            {
                KyLinValue new_var = new KyLinValue();
                new_var.setName(i);
                new_var.setIs_public(false);
                new_var.setType(KyLinType.getType(i , kyLinRuntime));
                new_var.setContent(new KyLinExpression().getExpression(i , this) , kyLinRuntime);

                arrayList.add(new_var);
            }
            kyLinList.arrayList = arrayList;

            KyLinValue kyLinValue = new KyLinValue();
            kyLinValue.setIs_public(isPublic);
            kyLinValue.setName(name);
            kyLinValue.setContent(kyLinList , kyLinRuntime);
            kyLinValue.setType("list");

            this.ValueMap.put(name , kyLinValue);
            return true;
        }
        else {
            return false;
        }
    }
    public void new_value(String code , boolean isPublic) throws Exception {
        //这个是定义变量的语句
        String name = code.substring(code.indexOf(" ")+1,code.indexOf("=")).trim();
        String content = code.substring(code.indexOf("=")+1).trim();

        if (this.PublicRuntime != null && this.PublicRuntime.ValueMap.containsKey(name))
        {
            if (this.new_class(code , content , isPublic , this.PublicRuntime )) {
                return;
            }else {
                KyLinValue kylinValue = new KyLinValue();
                kylinValue.setContent(content ,this);
                kylinValue.setName(name);
                kylinValue.setIs_public(isPublic);
                this.PublicRuntime.ValueMap.put(name , kylinValue);
            }
        }
        if (content.equals("null")) {
            KyLinValue kylinValue = new KyLinValue();
            kylinValue.setContent(null,this);
            kylinValue.setName(name);
            kylinValue.setIs_public(isPublic);
            this.ValueMap.put(name , kylinValue);
        }
        else {
            if (this.new_class(code , content , isPublic , this)) {
                return;
            }
            if (this.new_list(code , content , isPublic , this)) {
                return;
            }
            KyLinValue kylinValue = new KyLinValue();
            kylinValue.setContent(new KyLinExpression().getExpression(content , this) ,this);
            kylinValue.setName(name);
            kylinValue.setIs_public(isPublic);

            this.ValueMap.put(name , kylinValue);
        }
    }

    public KyLinRuntime() {

    }
}
