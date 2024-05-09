package Program;

import main.PathLoader;
import main.baseFunction;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import KylinException.KylinRuntimeException;
import main.mainApp;

public class KyLinRuntime {
    public ArrayList<String> code;
    public boolean isError = false;
    public final ConcurrentHashMap<String , KyLinValue> ValueMap = new ConcurrentHashMap<>();
    public final Map<String , KyLinFunction> FunctionMap = new HashMap<>();
    public final Map<String , KyLinFunction> ExceptionMap = new HashMap<>();
    public boolean isFunction = false;
    private KyLinValue result;
    public KyLinRuntime PublicRuntime;
    public int codeLine = 0;
    public KyLinValue getResult() {
        return this.result;
    }
    public final Map<String , String> defined_keyword = new HashMap<>();
    public Map<String , String> defined_func = new HashMap<>();
    public String name;
    private boolean isIf = false;
    private final boolean IF_OK = false;
    public final Map<String , KyLinClass> classMap = new HashMap<>();
    public boolean OnErrorExit = true;
    public final File code_file = null;
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
            this.result = new KyLinExpression().getObjectExpression(code.substring(code.indexOf("return ")+"return ".length()).trim(), this);
            try {
                if (isStream) {
                    process.destroy();
                }
            }catch(Exception e) {
                throw new RuntimeException(e);
            }
        } else if (code.startsWith("ref ")) {
            this.new_ref(code , true);
        }
        else if (code.startsWith("val ")) {
            new KyLinVal().Val(code,this);
        }
        else {
            final String substring = code.substring(code.indexOf(" ") + 1, code.indexOf("("));
            final String replace = code.substring(code.indexOf("(") + 1, code.lastIndexOf(")")).replace(" ", "");
            if (code.startsWith("func ") || code.startsWith("f ")) {
                String name = substring.trim();
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
                    kylinFunction.setInput(replace.split(",") , this);
    
                    ArrayList<String> functionCode = new ArrayList<>();
                    if (code.startsWith("func "))
                    {
                        for (int j = i+1 ; j < this.code.size() ;j++) {
                            String line = this.code.get(j).trim();
                            if (("end_func".equals(line))) {
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
                            if (("e_f".equals(line))) {
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
            }
            else if (KyLinProgramBaseFunction.isProgramBaseFunction(code, PublicRuntime))
            {
                KyLinProgramBaseFunction.runProgramBaseFunction(code , this);
            }
            else if (KyLinUseFunction.isUseFunction(code , this)) {
                KyLinUseFunction.UseFunction(code , this);
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
                    new KyLinExpression().getExpression(func,this);
                }
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
                String name = substring.trim();
                boolean isPublic = false;
    
                KyLinFunction kylinFunction = new KyLinFunction(name);
                kylinFunction.isPublic = isPublic;
    
                ArrayList<String> functionCode = new ArrayList<>();
                for (int j = i+1 ; j < this.code.size() ;j++)
                {
                    String line = this.code.get(j).trim();
                    if (("e_err".equals(line)))
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
                kylinFunction.err_code = replace +".message";
                KyLinValue kylinValue = new KyLinValue();
                kylinValue.setName(replace +".message");
                kylinFunction.kylinRuntime.ValueMap.put(replace +".message",kylinValue);
                this.ExceptionMap.put(name , kylinFunction);
            }
            else if (code.startsWith("class "))
            {
                String name = code.substring(code.indexOf(" ")+1,code.indexOf(":")).trim();
                boolean isPublic = baseFunction.isPublic(code.substring(code.lastIndexOf(":")+1).trim());
    
                KyLinClass kyLinClass = new KyLinClass(name);
                kyLinClass.isPublic = isPublic;
    
                for (int j = i + 1 ; j < this.code.size() ; j++)
                {
                    String line = this.code.get(j).trim();
                    if (line.isEmpty())
                    {
                        continue;
                    }
                    if ("end_class".equals(line))
                    {
                        this.codeLine = j;
                        break;
                    }
                    kyLinClass.code.add(line);
                }
                kyLinClass.init_class();
                this.classMap.put(name , kyLinClass);
            }
            else if (code.startsWith("import "))
            {
                String lib = new KyLinExpression().getExpression(code.substring(7) , this);
                String lib_path = PathLoader.getLibName(lib);
                ImportLib.lib_import(lib_path,this);
            }
            else if (code.startsWith("#include "))
            {
                baseFunction.include(code , this);
            }
            else if (isFunction(code))
            {
                runFunction(code);
            }
            else {
                KylinRuntimeException kylinRuntimeException = new KylinRuntimeException("code error.",this.codeLine,OnErrorExit);
                kylinRuntimeException.PrintErrorMessage(this);
                //new KyLinExpression().getExpression(code,this);
            }
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
            return KyLinExpression.getFunctionFromRuntime(function , this) != null;
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
                if (KyLinExpression.getFunctionFromRuntime(split[i].trim(), this) != null) {
                    //System.out.println(kylinFunction.kylinRuntime.ValueMap);
                    if (kylinFunction.kylinRuntime.PublicRuntime != null) {
                        kylinFunction.kylinRuntime.PublicRuntime.FunctionMap.put(input, KyLinExpression.getFunctionFromRuntime(split[i].trim(), this));
                    }
                    kylinFunction.kylinRuntime.FunctionMap.put(input, KyLinExpression.getFunctionFromRuntime(split[i].trim(), this));
                    continue;
                }
                KyLinValue kylinValue = new KyLinValue();
                kylinValue.setName(input);
                kylinValue.setContent(new KyLinExpression().getExpression(split[i] , this), this);
                kylinFunction.kylinRuntime.ValueMap.put(input,kylinValue);
            }
            kylinFunction.kylinRuntime.run();
        }
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
        if ("null".equals(content)) {
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
