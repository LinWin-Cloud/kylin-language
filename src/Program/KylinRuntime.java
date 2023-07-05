package Program;

import main.baseFunction;

import java.util.*;
import java.util.Map;
import java.util.Set;

import KylinException.KylinRuntimeException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class KylinRuntime {
    public ArrayList<String> code;
    public boolean isError = false;
    public Map<String , KylinValue> ValueMap = new HashMap<>();
    public Map<String , KylinFunction> FunctionMap = new HashMap<>();
    public Map<String , KylinFunction> ExceptionMap = new HashMap<>();
    public boolean isFunction = false;
    private String result = "";
    public KylinRuntime PublicRuntime;
    public int codeLine = 0;
    public String getResult() {
        return this.result;
    }
    public Map<String , String> defined_keyword = new HashMap<>();
    public Map<String , String> defined_func = new HashMap<>();
    public String name;
    private boolean isIf = false;
    private boolean IF_OK = false;

    public KylinRuntime(String name) {
        this.name = name;
    }

    public void exec(String code, int i) throws Exception {
        String[] words = code.trim().split(" ");
        if (code.equals("")) {
            return;
        }
        String keyword = this.defined_keyword.get(words[0]);
        if (keyword == null) {
            keyword = "";
        }
        if (code.startsWith("//")) {
            return;
        }
        //String keyword = this.defined_keyword.get(words[0]);
        //System.out.println(words[0]);
        if (words[0].equals("var") || keyword.equals("var")) {
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
