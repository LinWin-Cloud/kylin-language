package Program;


import main.mainApp;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class KylinExpression {
    public String getExpression(String code , KylinRuntime kylinRuntime) {
        try {
            String[] tokens = code.split("[<>]");
            StringBuffer stringBuffer = new StringBuffer();
            for (String s : tokens)
            {
                s = s.trim();
                if (s.equals("")) {
                    continue;
                }
                else if (s.startsWith("\"") && s.endsWith("\"")) {
                    stringBuffer.append(s, 1, s.length() - 1);
                    continue;
                }
                else if (kylinRuntime.ValueMap.containsKey(s)) {
                    KylinValue kylinValue = kylinRuntime.ValueMap.get(s);
                    if (kylinValue.getContent() == null) {
                        throw new Exception("ERR Value: "+s+" . At Line: "+(kylinRuntime.PublicRuntime.codeLine+1));
                    }else {
                        stringBuffer.append(kylinRuntime.ValueMap.get(s).getContent());
                    }
                    continue;
                }
                else if(KylinProgramBaseFunction.isRealDefinedFunction(code,kylinRuntime)) {
                    String function = code.substring(0 , code.indexOf("(")).trim();
                    String input = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
                    KylinFunction kylinFunction = kylinRuntime.FunctionMap.get(function);

                    String[] func_content = input.split(",\\s*");
                    for (int j = 0 ; j < kylinFunction.input.length ; j++) {
                        KylinValue kylinValue = new KylinValue();
                        kylinValue.setName(kylinFunction.input[j]);
                        kylinValue.setContent(func_content[j] , kylinRuntime);
                        kylinFunction.kylinRuntime.ValueMap.put(kylinFunction.input[j] , kylinValue);
                    }
                    kylinFunction.kylinRuntime.run();
                    stringBuffer.append(kylinFunction.kylinRuntime.getResult());
                    continue;
                }
                else if (KylinProgramBaseFunction.isDefinedFunction(code) && KylinUseFunction.isUseFunction(s)) {
                    stringBuffer.append(KylinUseFunction.UseFunction(code , kylinRuntime));
                    continue;
                }
                else if (KylinProgramBaseFunction.isDefinedFunction(code) && !KylinProgramBaseFunction.isRealDefinedFunction(code,kylinRuntime))
                {
                    String function = code.substring(0 , code.indexOf("(")).trim();
                    throw new Exception("ERR Function: "+function);
                }
                else if (kylinRuntime.PublicRuntime != null
                    && kylinRuntime.PublicRuntime.ValueMap.containsKey(s)
                ) {
                    stringBuffer.append(kylinRuntime.PublicRuntime.ValueMap.get(s).getContent());
                    continue;
                }
                else {
                    try {
                        double math = this.evaluateExpression(s);
                        stringBuffer.append(math);
                    }catch (Exception exception) {
                        stringBuffer.append(s);
                    }
                }
            }
            //System.out.println(stringBuffer.toString());
            return stringBuffer.toString();
        }
        catch (Exception exception) {
            //exception.printStackTrace();
            System.out.println("[ERR] "+exception.getMessage());
            System.exit(1);
            return null;
        }
    }
    public String[] getListExpression(String[] list , KylinRuntime kylinRuntime) {
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
    public double evaluateExpression(String expression) throws IllegalArgumentException {
        try {
            Object evalResult = mainApp.scriptEngine.eval(expression);
            if (evalResult instanceof Integer) {
                return (Integer) evalResult;
            } else if (evalResult instanceof Double) {
                return (Double) evalResult;
            } else {
                throw new IllegalArgumentException("无效的表达式");
            }
        } catch (ScriptException e) {
            throw new IllegalArgumentException("无效的表达式");
        }
    }
}
