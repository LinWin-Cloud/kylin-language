package Program;


import KylinException.KylinRuntimeException;
import main.mainApp;

import javax.script.ScriptException;
import java.util.ArrayList;

public class KyLinExpression {
    public String getExpression(String code , KyLinRuntime kylinRuntime) {
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
                    KyLinValue kylinValue = kylinRuntime.ValueMap.get(s);
                    if (kylinValue.getContent() == null) {
                        throw new Exception("ERR Value: "+s+" . At Line: "+(kylinRuntime.PublicRuntime.codeLine+1));
                    }else {
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
                else if (KyLinProgramBaseFunction.isDefinedFunction(code) && !KyLinProgramBaseFunction.isRealDefinedFunction(code,kylinRuntime))
                {
                    String function = code.substring(0 , code.indexOf("(")).trim();
                    try {
                        double math = this.evaluateExpression(s , kylinRuntime);
                        stringBuffer.append(math);
                    }catch (Exception exception) {
                        throw new Exception("ERR Expression: "+function);
                    }
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
                }
            }
            //System.out.println(stringBuffer.toString());
            return stringBuffer.toString();
        }
        catch (StackOverflowError stackOverflowError) {
            KylinRuntimeException kylinRuntimeException =
                    new KylinRuntimeException("StackOverflowError" , kylinRuntime.codeLine+1 , true);
            kylinRuntimeException.PrintErrorMessage(kylinRuntime);
            return null;
        }
        catch (Exception exception) {
            KylinRuntimeException kylinRuntimeException =
                    new KylinRuntimeException(exception.getMessage() , kylinRuntime.codeLine+1 , true);
            kylinRuntimeException.PrintErrorMessage(kylinRuntime);
            return null;
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
    public double evaluateExpression(String expression , KyLinRuntime kylinRuntime) throws IllegalArgumentException {
        try {
            String[] exp = expression.split("(?=[+\\-*/()])|(?<=[+\\-*/()])");
            String code = expression;
            StringBuffer stringBuffer = new StringBuffer();
            for (String s:exp) {
                s = s.trim();
                if (s.equals("")) {
                    continue;
                }
                else if (s.startsWith("\"") && s.endsWith("\"")) {
                    stringBuffer.append(s, 1, s.length() - 1);
                    continue;
                }
                else if (kylinRuntime.ValueMap.containsKey(s)) {
                    KyLinValue kylinValue = kylinRuntime.ValueMap.get(s);
                    if (kylinValue.getContent() == null) {
                        throw new Exception("ERR Value: "+s+" . At Line: "+(kylinRuntime.PublicRuntime.codeLine+1));
                    }else {
                        stringBuffer.append(kylinRuntime.ValueMap.get(s).getContent());
                    }
                    continue;
                }
                else if(KyLinProgramBaseFunction.isRealDefinedFunction(code,kylinRuntime)) {
                    String function = code.substring(0 , code.indexOf("(")).trim();
                    String input = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
                    KyLinFunction kylinFunction = kylinRuntime.FunctionMap.get(function);

                    String[] func_content = input.split(",\\s*");
                    for (int j = 0 ; j < kylinFunction.input.length ; j++) {
                        KyLinValue kylinValue = new KyLinValue();
                        kylinValue.setName(kylinFunction.input[j]);
                        kylinValue.setContent(func_content[j] , kylinRuntime);
                        kylinFunction.kylinRuntime.ValueMap.put(kylinFunction.input[j] , kylinValue);
                    }
                    kylinFunction.kylinRuntime.run();
                    stringBuffer.append(kylinFunction.kylinRuntime.getResult());
                    continue;
                }
                else if (KyLinProgramBaseFunction.isDefinedFunction(code) && KyLinUseFunction.isUseFunction(s,kylinRuntime)) {
                    stringBuffer.append(KyLinUseFunction.UseFunction(s , kylinRuntime));
                    continue;
                }
                else if (KyLinProgramBaseFunction.isDefinedFunction(code) && !KyLinProgramBaseFunction.isRealDefinedFunction(code,kylinRuntime))
                {
                    String function = code.substring(0 , code.indexOf("(")).trim();
                    try {
                        double math = this.evaluateExpression(s , kylinRuntime);
                        stringBuffer.append(math);
                    }catch (Exception exception) {
                        throw new Exception("ERR Expression: "+function);
                    }
                }
                else if (kylinRuntime.PublicRuntime != null
                        && kylinRuntime.PublicRuntime.ValueMap.containsKey(s)
                ) {
                    stringBuffer.append(kylinRuntime.PublicRuntime.ValueMap.get(s).getContent());
                    continue;
                }else {
                    stringBuffer.append(s);
                }
            }
            Object evalResult = mainApp.scriptEngine.eval(stringBuffer.toString());
            if (evalResult instanceof Integer) {
                return (Integer) evalResult;
            } else if (evalResult instanceof Double) {
                return (Double) evalResult;
            } else {
                throw new IllegalArgumentException("Invalid expression");
            }
        } catch (ScriptException e) {
            throw new IllegalArgumentException("Invalid expression");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
