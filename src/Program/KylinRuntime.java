package Program;

import main.baseFunction;

import java.util.*;
import java.util.Map;
import java.util.Set;

public class KylinRuntime {
    public ArrayList<String> code;
    public Map<String , KylinValue> ValueMap = new HashMap<>();
    public Map<String , KylinFunction> FunctionMap = new HashMap<>();
    public boolean isFunction = false;
    private String result = "";
    public KylinRuntime PublicRuntime;
    public int codeLine = 0;
    public String getResult() {
        return this.result;
    }

    public void exec(String code, int i) throws Exception {
        String[] words = code.trim().split(" ");
        if (code.equals("")) {
            return;
        }
        if (words[0].equals("var")) {
            String name = words[1];
            String content = code.substring(code.indexOf("=")+1).trim();
            KylinValue kylinValue = new KylinValue();
            kylinValue.setContent(content ,this);
            kylinValue.setName(name);
            this.ValueMap.put(name , kylinValue);
            return;
        }
        if (isFunction && words[0].equals("return")) {
            this.result = new KylinExpression().getExpression(code.substring(code.indexOf("return ")+"return ".length()).trim(), this);
            return;
        }
        else if (words[0].equals("func")) {
            /**
             * func func_name (a ,b) public
             *      return <a + b>
             * end_func
             */
            String name = code.substring(code.indexOf(" ")+1,code.indexOf("(")).trim();
            String inputContent = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).replace(" ","");
            boolean isPublic = baseFunction.isPublic(code.substring(code.lastIndexOf(")")+1).trim());

            KylinFunction kylinFunction = new KylinFunction();
            kylinFunction.isPublic = isPublic;
            kylinFunction.setInput(inputContent.split(","));

            ArrayList<String> functionCode = new ArrayList<>();
            for (int j = i+1 ; j < this.code.size() ;j++) {
                String line = this.code.get(j).trim();
                if (line.equals("end_func")) {
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
            this.FunctionMap.put(name , kylinFunction);
            return;
        }
        else if (words[0].equals("f")) {
            String name = code.substring(code.indexOf(" ")+1,code.indexOf("(")).trim();
            String inputContent = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).replace(" ","");
            boolean isPublic = false;

            KylinFunction kylinFunction = new KylinFunction();
            kylinFunction.isPublic = isPublic;
            kylinFunction.setInput(inputContent.split(","));

            ArrayList<String> functionCode = new ArrayList<>();
            for (int j = i+1 ; j < this.code.size() ;j++) {
                String line = this.code.get(j).trim();
                if (line.equals("e_f")) {
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
            this.FunctionMap.put(name , kylinFunction);
            return;
        }
        else if (isFunction(code)) {
            return;
        }
        else if (KylinProgramBaseFunction.isProgramBaseFunction(code , this)) {
            return;
        }
        else {
            System.out.println("Syntax Error: "+code+" :: At Line: "+(codeLine+1));
            System.exit(1);
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
            if (this.FunctionMap.containsKey(function)) {
                KylinFunction kylinFunction =this.FunctionMap.get(function);
                String[] split = content.split(",\\s*");
                // System.out.println(kylinFunction.input.length);
                // System.out.println(split.length);
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
        }catch (Exception exception) {
            //exception.printStackTrace();
            return false;
        }
    }
}
