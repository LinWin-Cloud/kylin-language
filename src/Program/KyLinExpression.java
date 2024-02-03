package Program;


import KylinException.KylinRuntimeException;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;

public class KyLinExpression {
    public String getExpression(String code , KyLinRuntime kylinRuntime) throws Exception {
        try {
            // ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)"
            //System.out.println(this.isList(code , kylinRuntime)+" "+code);
            String[] tokens = code.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");
            StringBuilder stringBuffer = new StringBuilder();

            for (String s : tokens)
            {
                s = s.trim();
                if (s.isEmpty()) {
                    continue;
                }
                else if (this.isNumber(s)) {
                    stringBuffer.append(s);
                    continue;
                }
                else if (s.startsWith("\"") && s.endsWith("\"")) {
                    try {
                        stringBuffer.append(this.returnString(s.substring(1,s.length() -1)));
                        continue;
                    }catch (Exception exception) {
                        throw new Exception("string error.");
                    }
                }
                else if (this.isList(code , kylinRuntime)) {
                    try {
                        String function = code.substring(0 , code.indexOf("[")).trim();
                        String input = code.substring(code.indexOf("[")+1 , code.lastIndexOf("]")).trim();
                        if (kylinRuntime.ValueMap.containsKey(function))
                        {
                            KyLinList kyLinList = (KyLinList) kylinRuntime.ValueMap.get(function).getContent();
                            stringBuffer.append(kyLinList.arrayList.get(Integer.parseInt(input)).getContent());
                        }
                        else if (kylinRuntime.PublicRuntime != null && kylinRuntime.ValueMap.containsKey(function))
                        {
                            KyLinList kyLinList = (KyLinList) kylinRuntime.PublicRuntime.ValueMap.get(function).getContent();
                            stringBuffer.append(kyLinList.arrayList.get(Integer.parseInt(input)).getContent());
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception(e);
                    }
                }
                else if (kylinRuntime.ValueMap.containsKey(s)) {
                    KyLinValue kylinValue = kylinRuntime.ValueMap.get(s);
                    if (kylinValue == null) {
                        throw new Exception("ERR Value: "+s+" . At Line: "+(kylinRuntime.PublicRuntime.codeLine+1));
                    }
                    else if (kylinValue.getContent() == null ) {
                        stringBuffer.append("null");
                    }
                    else {
                        stringBuffer.append(kylinRuntime.ValueMap.get(s).getContent());
                    }
                    continue;
                }
                else if(KyLinProgramBaseFunction.isRealDefinedFunction(code,kylinRuntime)) {
                    String function = code.substring(0 , code.indexOf("(")).trim();
                    String input = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
                    KyLinFunction kylinFunction = kylinRuntime.FunctionMap.get(function);
                    KyLinFunction PubFunc = null;
                    String[] func_content = input.split(",\\s*");

                    if (kylinFunction == null && (kylinRuntime.PublicRuntime!=null&&kylinRuntime.PublicRuntime.FunctionMap.containsKey(function))) {
                        PubFunc = kylinRuntime.PublicRuntime.FunctionMap.get(function);
                    }
                    if (PubFunc == null) {
                        for (int j = 0 ; j < kylinFunction.input.length ; j++) {
                            KyLinValue kylinValue = new KyLinValue();
                            kylinValue.setName(kylinFunction.input[j]);
                            kylinValue.setContent(func_content[j] , kylinRuntime);
                            kylinFunction.kylinRuntime.ValueMap.put(kylinFunction.input[j] , kylinValue);
                        }
                        kylinFunction.kylinRuntime.run();
                        stringBuffer.append(kylinFunction.kylinRuntime.getResult());
                    }
                    else {
                        for (int j = 0 ; j < PubFunc.input.length ; j++) {
                            KyLinValue kylinValue = new KyLinValue();
                            kylinValue.setName(PubFunc.input[j]);
                            kylinValue.setContent(func_content[j] , kylinRuntime);
                            PubFunc.kylinRuntime.ValueMap.put(PubFunc.input[j] , kylinValue);
                        }
                        PubFunc.kylinRuntime.run();
                        stringBuffer.append(PubFunc.kylinRuntime.getResult());
                    }
                    continue;
                }
                else if (KyLinProgramBaseFunction.isDefinedFunction(code) && KyLinUseFunction.isUseFunction(s , kylinRuntime)) {
                    stringBuffer.append(KyLinUseFunction.UseFunction(s , kylinRuntime));
                    continue;
                }
                else if (kylinRuntime.PublicRuntime != null
                    && kylinRuntime.PublicRuntime.ValueMap.containsKey(s)
                ) {
                    stringBuffer.append(kylinRuntime.PublicRuntime.ValueMap.get(s).getContent());
                    continue;
                }
                else {
                    try {
                        double math = this.evaluateExpression(s , kylinRuntime);
                        stringBuffer.append(math);
                    }catch (Exception exception) {
                        stringBuffer.append(s);
                    }
                    continue;
                }
            }
            return stringBuffer.toString();
        }
        catch (StackOverflowError stackOverflowError) {
            KylinRuntimeException kylinRuntimeException =
                    new KylinRuntimeException("StackOverflowError" , kylinRuntime.codeLine+1 , true);
            kylinRuntimeException.PrintErrorMessage(kylinRuntime);
            return null;
        }
        catch (Exception exception) {
            //exception.printStackTrace();
            /**
             KylinRuntimeException kylinRuntimeException =
             new KylinRuntimeException("Expression error" , kylinRuntime.codeLine+1 , true);
             kylinRuntimeException.PrintErrorMessage(kylinRuntime);
             */
            throw new Exception(exception.getMessage());
        }
    }
    public String returnString(String str){
        str = str.replace("\\n","\n");
        return str;
    }
    public boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        }catch (Exception exception) {
            return false;
        }
    }
    private boolean isList(String code , KyLinRuntime kyLinRuntime) throws Exception {
        try {
            String function = code.substring(0 , code.indexOf("[")).trim();
            String input = code.substring(code.indexOf("[")+1 , code.lastIndexOf("]")).trim();

            if (kyLinRuntime.ValueMap.containsKey(function))
            {
                return true;
            }
            else if (kyLinRuntime.PublicRuntime != null && kyLinRuntime.ValueMap.containsKey(function))
            {
                return true;
            }else {
                throw new Exception("No List: "+function);
            }
        }catch (Exception exception) {
            return false;
        }
    }
    public String[] getListExpression(String[] list , KyLinRuntime kylinRuntime) {
        try {
            ArrayList<String> stringArrayList = new ArrayList<>();
            for (String s : list) {
                stringArrayList.add(this.getExpression(s,kylinRuntime));
            }
            return stringArrayList.toArray(new String[stringArrayList.size()]);
        }catch (Exception exception) {
            System.out.println("[ERR] "+exception.getMessage());
            System.exit(1);
            return null;
        }
    }
    public double evaluateExpression(String expression , KyLinRuntime kylinRuntime) throws Exception {
        try {
            String[] exp = expression.split("(?=[+\\-*/()])|(?<=[+\\-*/()])");

            StringBuilder stringBuffer = new StringBuilder();
            for (String s:exp) {
                s = s.trim();
                if (s.isEmpty()) {
                    continue;
                }
                else if (kylinRuntime.ValueMap.containsKey(s)) {
                    KyLinValue kylinValue = kylinRuntime.ValueMap.get(s);
                    if (kylinValue == null) {
                        throw new Exception("ERR Value: "+s+" . At Line: "+(kylinRuntime.PublicRuntime.codeLine+1));
                    }
                    else if (kylinValue.getContent() == null) {
                        stringBuffer.append("");
                    }
                    else {
                        stringBuffer.append(kylinRuntime.ValueMap.get(s).getContent());
                    }
                    continue;
                }
                else if(KyLinProgramBaseFunction.isRealDefinedFunction(expression,kylinRuntime)) {
                    String function = expression.substring(0 , expression.indexOf("(")).trim();
                    String input = expression.substring(expression.indexOf("(")+1 , expression.lastIndexOf(")")).trim();
                    KyLinFunction kylinFunction = kylinRuntime.FunctionMap.get(function);

                    String[] func_content = input.split(",\\s*");
                    for (int j = 0 ; j < kylinFunction.input.length ; j++) {
                        KyLinValue kylinValue = new KyLinValue();
                        kylinValue.setName(kylinFunction.input[j]);
                        kylinValue.setContent(this.getExpression(func_content[j] , kylinRuntime) , kylinRuntime);
                        kylinFunction.kylinRuntime.ValueMap.put(kylinFunction.input[j] , kylinValue);
                    }
                    kylinFunction.kylinRuntime.run();
                    stringBuffer.append(kylinFunction.kylinRuntime.getResult());
                    continue;
                }
                else if (KyLinProgramBaseFunction.isDefinedFunction(expression) && KyLinUseFunction.isUseFunction(s,kylinRuntime)) {
                    stringBuffer.append(KyLinUseFunction.UseFunction(s , kylinRuntime));
                    continue;
                }
                else if (kylinRuntime.PublicRuntime != null
                        && kylinRuntime.PublicRuntime.ValueMap.containsKey(s)
                ) {
                    stringBuffer.append(kylinRuntime.PublicRuntime.ValueMap.get(s).getContent());
                    continue;
                }
                else {
                    stringBuffer.append(s);
                }
            }
            return StringParser.evaluateExpression(stringBuffer.toString());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
