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
    public void exec(String code, int i) throws Exception {
        String[] words = code.trim().split(" ");
        if (words[0].equals("var")) {
            String name = words[1];
            String content = code.substring(code.indexOf("=")+1).trim();
            KylinValue kylinValue = new KylinValue();
            kylinValue.kylinRuntime = this;
            kylinValue.setContent(content);
            kylinValue.setName(name);
            this.ValueMap.put(name , kylinValue);
        }
        if (words[0].equals("func")) {
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
                    break;
                }
                functionCode.add(line);
            }
            kylinFunction.kylinRuntime.code = functionCode;
            this.FunctionMap.put(name , kylinFunction);
        }
        if (isFunction(code)) {
            return;
        }
        if (KylinProgramBaseFunction.isProgramBaseFunction(code)) {
            return;
        }
    }
    public void run() throws Exception {
        for (int i = 0 ; i < this.code.size() ;i++) {
            this.exec(this.code.get(i) , i);
        }
    }
    public boolean isFunction(String code) {
        try {
            String function = code.substring(0,code.indexOf("(")).trim();
            if (this.FunctionMap.containsKey(function)) {
                this.FunctionMap.get(function).kylinRuntime.run();
                return true;
            }else {
                return false;
            }
        }catch (Exception exception) {
            return false;
        }
    }
}
