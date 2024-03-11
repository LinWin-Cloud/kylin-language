package Program;

import main.PathLoader;
import main.baseFunction;

import java.io.File;
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
    public File code_file = null;                                               // 该运行环境的文件
    public KyLinRuntime(String name) {
        this.name = name;
    }
    public boolean isClass = false;
    public boolean isStream = false;
    public Process process = null;

    public void exec(String code, int i) throws Exception
    {
        if (code.isEmpty())
        {
            return;
        }
        if (code.startsWith("//"))
        {
            // 这条是处理注释，是注释的直接过去
            return;
        }
        if (code.startsWith("#"))
        {
            // 这条是处理注释，是注释的直接过去
            return;
        }
        if (code.startsWith("var "))
        {
            this.new_value(code,true);
            return;
        }
        if (isFunction && (code.startsWith("return ")))
        {
            this.result = new KyLinExpression().getExpression(code.substring(code.indexOf("return ")+"return ".length()).trim(), this);
            try {
                if (isStream) {
                    process.destroy();
                }
            }catch(Exception e) {
                return;
            }
            return;
        }
        /**
         *  在 kylin 编程语言 3.0 中删除对 #func 和 #defined 的支持，因为这些特性用处不大而且极大地拖慢了 kylin vm的运行速度.
         *         else if (code.startsWith("#func") || keyword.equals("#func"))
         *         {
         *             String key = words[1];
         *             String value = words[2];
         *             this.defined_func.put(value , key);
         *             return;
         *         }
         */
        else if (code.startsWith("ref ")) {
            /**
             * 该标准在 kylin3.2 发布，用于共享指针和对象，就是两个变量用的是一个指针
             * var a = 1
             * ref b = a
             *
             * val b = 2
             * print(a)         //输出 a 为 2
             */
            this.new_ref(code , true);
        }
        else if (code.startsWith("val ")) {
            new KyLinVal().Val(code,this);
            return;
        }
        else if (code.startsWith("func ") || code.startsWith("f ")) {
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

                if (code.startsWith("func"))
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
                if (code.startsWith("func "))
                {
                    for (int j = i+1 ; j < this.code.size() ;j++) {
                        String line = this.code.get(j).trim();
                        if ((line.equals("end_func"))) {
                            this.codeLine = j;
                            break;
                        }
                        if (line.isEmpty()) {
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
                        if (line.isEmpty()) {
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
        else if (KyLinProgramBaseFunction.runProgramBaseFunction(code , this))
        {
            return;
        }
        else if (code.startsWith("if "))
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
        else if (code.startsWith("else ") && this.isIf && !this.IF_OK)
        {
            this.isIf = false;
            String func = code.substring(code.indexOf(" ")+1);
            new KyLinExpression().getExpression(func,this);
        }
        else if (code.startsWith("err "))
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
                if (line.isEmpty())
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
        else if (code.startsWith("class "))
        {
            String name = code.substring(code.indexOf(" ")+1,code.indexOf(":")).trim();
            boolean isPublic = main.baseFunction.isPublic(code.substring(code.lastIndexOf(":")+1).trim());

            KyLinClass kyLinClass = new KyLinClass(name);
            kyLinClass.isPublic = isPublic;

            for (int j = i + 1 ; j < this.code.size() ; j++)
            {
                String line = this.code.get(j).trim();
                if (line.isEmpty())
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
        else if (code.startsWith("import "))
        {
            String lib = new KyLinExpression().getExpression(code.substring(7) , this);
            String lib_path = PathLoader.getLibName(lib);
            ImportLib.lib_import(lib_path,this);
            return;
        }
        else if (code.startsWith("#include "))
        {
            main.baseFunction.include(code , this);
            return;
        }
        else if (isFunction(code))
        {
            runFunction(code);
            return;
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
    public void runFunction(String code) throws Exception {
        String function = code.substring(0, code.indexOf("(")).trim();
        String content = code.substring(code.indexOf("(") + 1, code.lastIndexOf(")")).trim();

        if (KyLinExpression.getFunctionFromRuntime(function , this) != null) {

            KyLinFunction kylinFunction = KyLinExpression.getFunctionFromRuntime(function , this);
            String[] split = content.split(",(?![^(]*\\))");
            assert kylinFunction != null;
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
        }
        /*
        else if (this.PublicRuntime != null && this.PublicRuntime.FunctionMap.containsKey(function)) {
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
        }
         */
    }
    public void new_ref(String code , boolean isPublic) throws Exception {
        String name = code.substring(code.indexOf(" ")+1,code.indexOf("=")).trim();
        String content = code.substring(code.indexOf("=")+1).trim();

        if (this.ValueMap.containsKey(name)) {
            throw new Exception("The Value: "+name+" was defined.");
        }
        else {
            if (this.ValueMap.containsKey(content)) {
                KyLinValue clone_value = this.ValueMap.get(content);
                KyLinValue value = new KyLinValue();

                value.setContent(clone_value.getContent() , this);
                value.setType(clone_value.getType());
                value.setName(name);
                value.setIs_public(isPublic);
                value.setPointer(clone_value.getPointer());

                this.ValueMap.put(value.getName() , value);
            }
            else if (this.PublicRuntime != null && this.PublicRuntime.ValueMap.containsKey(content)) {
                KyLinValue clone_value = this.PublicRuntime.ValueMap.get(content);
                KyLinValue value = new KyLinValue();

                value.setContent(clone_value.getContent() , this);
                value.setType(clone_value.getType());
                value.setName(name);
                value.setIs_public(isPublic);
                value.setPointer(clone_value.getPointer());

                this.PublicRuntime.ValueMap.put(value.getName() , value);
            }
            else {
                throw new Exception("No Value: "+content);
            }
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
                this.ValueMap.put(name , value);
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

            ArrayList<KyLinValue> arrayList = new ArrayList<>();
            if (!new_list.isEmpty()) {
                String[] tmp = new_list.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");
                for (String i : tmp)
                {
                    KyLinValue new_var = new KyLinValue();
                    new_var.setName(i);
                    new_var.setIs_public(false);
                    new_var.setType(KyLinType.getType(i , kyLinRuntime));
                    new_var.setContent(new KyLinExpression().getExpression(i , this) , kyLinRuntime);

                    arrayList.add(new_var);
                }
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
    public boolean isListGet(String code , String content , boolean isPublic , KyLinRuntime kylinRuntime) throws Exception {
        if (content.replace(" ","").startsWith("list_get(") && content.endsWith(")")) {
            String name = code.substring(code.indexOf(" ")+1 , code.indexOf("=")).trim();
            String get_Object = content.substring(content.indexOf("(")+1,content.lastIndexOf(")")).trim();
            String[] split = get_Object.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");
            String list_object = split[0].trim();
            String list_index = split[1].trim();
            //System.out.println(list_object+";"+list_index+";");
            String address = KyLinUseFunction.getAddress(list_object , kylinRuntime).toString();
            KyLinValue k = mainApp.all_kylin_value_pointer.get(address);

            KyLinValue value = new KyLinValue();

            System.out.println(k.getPointer()+" "+address);
            KyLinList list = (KyLinList) k.getContent();
            KyLinValue kv = list.arrayList.get(Integer.parseInt(list_index));
            value.setType(kv.getType());
            value.setIs_public(isPublic);
            value.setName(name);
            value.setContent(kv.getContent() , kylinRuntime);
            this.ValueMap.put(name , value);
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
            if (this.isListGet(code , content , isPublic , this)) {
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
