package Program;


import KylinException.KylinRuntimeException;
import main.mainApp;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KyLinExpression{
    public static KyLinValue getValueFromRuntime(String name , KyLinRuntime runtime) {
        if (runtime.ValueMap.containsKey(name)) {
            return runtime.ValueMap.get(name);
        }
        else if (!runtime.ValueMap.containsKey(name) && runtime.PublicRuntime != null) {
            return getValueFromRuntime(name , runtime.PublicRuntime);
        } else {
            return null;
        }
    }

    public static KyLinFunction getFunctionFromRuntime(String name , KyLinRuntime runtime) {
        if (runtime.FunctionMap.containsKey(name)) {
            return runtime.FunctionMap.get(name);
        }
        else if (!runtime.FunctionMap.containsKey(name) && runtime.PublicRuntime != null) {
            return getFunctionFromRuntime(name , runtime.PublicRuntime);
        } else {
            return null;
        }
    }
    public static String init_str(String str) {
        String s = str.replace("\\'","\"");
        s = s.replace("\\n", "\n");
        return s;
    }
    public KyLinValue getObjectExpression(String code , KyLinRuntime kylinRuntime) throws Exception {
        try {
            Matcher matcher = mainApp.pattern_split_expression.matcher(code);
            String[] tokens = matcher.pattern().split(code);
            if (tokens.length >= 2) {
                String s = this.getExpression(code , kylinRuntime);
                KyLinValue value = new KyLinValue();
                value.setType("object");
                value.setIs_public(true);
                value.setContent(s , kylinRuntime);
                return value;
            }else {
                if (this.isList(code , kylinRuntime)) {
                    try {
                        String function = code.substring(0 , code.indexOf("[")).trim();
                        String input = code.substring(code.indexOf("[")+1 , code.lastIndexOf("]")).trim();
                        KyLinValue value = getValueFromRuntime(function , kylinRuntime);
                        KyLinValue v = new KyLinValue();
                        try {
                            assert value != null;
                            KyLinList kyLinList = (KyLinList) value.getContent();
                            return  kyLinList.arrayList.get(Integer.parseInt(this.getExpression(input,kylinRuntime)));
                        }catch (Exception exception) {
                            String list_string = value.getContent().toString();
                            String s = list_string.substring(1 , list_string.length()-1).split(",")[Integer.parseInt(this.getExpression(input,kylinRuntime))];
                            v.setType("object");
                            v.setIs_public(true);
                            v.setContent(s , kylinRuntime);
                            return v;
                        }
                    }catch (Exception e) {
                        //e.printStackTrace();
                        throw new Exception(e);
                    }
                }
                else {
                    return null;
                }
            }
        }catch (Exception e) {
            throw new Exception(e);
        }
    }
    public String getExpression(String code , KyLinRuntime kylinRuntime) throws Exception {
        try {
            // ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)"
            Matcher matcher = mainApp.pattern_split_expression.matcher(code);
            String[] tokens = matcher.pattern().split(code);
            StringBuilder stringBuffer = new StringBuilder();

            for(String s : tokens)
            {
                s = s.trim();
                if (!s.isEmpty()) {
                    if (this.isNumber(s)) {
                        stringBuffer.append(s);
                    }
                    else if (s.startsWith("\"") && s.endsWith("\"")) {
                        try {
                            String ss = s.substring(1,s.length()-1);
                            stringBuffer.append(init_str(ss));
                        }catch (Exception exception) {
                            throw new Exception("string error.");
                        }
                    }
                    else if (this.isList(code , kylinRuntime)) {
                        try {
                            String function = code.substring(0 , code.indexOf("[")).trim();
                            String input = code.substring(code.indexOf("[")+1 , code.lastIndexOf("]")).trim();
                            KyLinValue value = getValueFromRuntime(function , kylinRuntime);
                            try {
                                assert value != null;
                                KyLinList kyLinList = (KyLinList) value.getContent();

                                stringBuffer.append(kyLinList.arrayList.get(Integer.parseInt(this.getExpression(input,kylinRuntime))).getContent());
                            }catch (Exception exception) {
                                String list_string = value.getContent().toString();
                                stringBuffer.append(list_string.substring(1 , list_string.length()-1).split(",")[Integer.parseInt(this.getExpression(input,kylinRuntime))]);
                            }
                        }catch (Exception e) {
                            //e.printStackTrace();
                            throw new Exception(e);
                        }
                    }
                    else if (getValueFromRuntime(s , kylinRuntime) != null) {
                        KyLinValue kylinValue = getValueFromRuntime(s , kylinRuntime);
                        if (kylinValue == null) {
                            throw new Exception("ERR Value: "+s+" . At Line: "+(kylinRuntime.codeLine+1));
                        }
                        else if (kylinValue.getContent() == null ) {
                            stringBuffer.append("null");
                        }
                        else {
                            stringBuffer.append(kylinValue.getContent());
                        }
                    }
                    else if(KyLinProgramBaseFunction.isRealDefinedFunction(s,kylinRuntime)) {
                        //System.out.println(1);
                        String function = s.substring(0 , s.indexOf("(")).trim();
                        String input = s.substring(s.indexOf("(")+1 , s.lastIndexOf(")")).trim();
                        KyLinFunction kylinFunction = KyLinExpression.getFunctionFromRuntime(function, kylinRuntime);
                        String[] func_content = input.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)(?=([^\\(]*\\([^\\)]*\\))*[^\\)]*$)");
                        //System.out.println(kylinFunction);
                        for (int j = 0; j < Objects.requireNonNull(kylinFunction).input.length ; j++) {
                            KyLinValue kylinValue = new KyLinValue();
                            kylinValue.setName(kylinFunction.input[j]);
                            kylinValue.setContent(func_content[j] , kylinRuntime);
                            kylinFunction.kylinRuntime.ValueMap.put(kylinFunction.input[j] , kylinValue);
                        }
                        kylinFunction.kylinRuntime.run();
                        stringBuffer.append(kylinFunction.kylinRuntime.getResult());
                    }
                    else if (KyLinProgramBaseFunction.isDefinedFunction(code) && KyLinUseFunction.isUseFunction(s , kylinRuntime)) {
                        stringBuffer.append(KyLinUseFunction.UseFunction(s , kylinRuntime));
                    }
                    /*
                    else if (kylinRuntime.PublicRuntime != null
                        && kylinRuntime.PublicRuntime.ValueMap.containsKey(s)
                    ) {
                        stringBuffer.append(kylinRuntime.PublicRuntime.ValueMap.get(s).getContent());
                        continue;
                    }
                     */
                    else {
                        try {
                            double math = this.evaluateExpression(s , kylinRuntime);
                            stringBuffer.append(math);
                        }catch (Exception exception) {
                            if ("true".equals(s) || "false".equals(s)) {
                                stringBuffer.append(s);
                            }else {
                                throw new Exception("Error Syntax: '"+ s +"' in "+code);
                            }
                        }
                    }
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
            throw new Exception(exception.getMessage());
        }
    }

    public boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        }catch (Exception exception) {
            return false;
        }
    }
    private boolean isList(String code , KyLinRuntime kyLinRuntime) {
        try {
            String function = code.substring(0 , code.indexOf("[")).trim();
            String input = code.substring(code.indexOf("[")+1 , code.lastIndexOf("]")).trim();
            return true;
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
            return stringArrayList.toArray(new String[0]);
        }catch (Exception exception) {
            System.out.println("[ERR] "+exception.getMessage());
            System.exit(1);
            return null;
        }
    }
    public static final Pattern pattern_math_exp = Pattern.compile("(?=[+\\-*/()])|(?<=[+\\-*/()])");
    public double evaluateExpression(String expression , KyLinRuntime kylinRuntime) throws Exception {
        try {
            String[] exp = pattern_math_exp.split(expression);

            StringBuilder stringBuffer = new StringBuilder();
            for (String s:exp) {
                s = s.trim();
                KyLinValue v = getValueFromRuntime(s , kylinRuntime);
                if (v == null) {
                    if(KyLinProgramBaseFunction.isRealDefinedFunction(expression,kylinRuntime)) {
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
                    }
                    else if (KyLinProgramBaseFunction.isDefinedFunction(expression) && KyLinUseFunction.isUseFunction(s,kylinRuntime)) {
                        stringBuffer.append(KyLinUseFunction.UseFunction(s , kylinRuntime));
                    }
                    else {
                        stringBuffer.append(s);
                    }
                } else {
                    //System.out.println(v);
                    if (v.getContent() != null) {
                        stringBuffer.append(v.getContent());
                    }
                }
            }
            return StringParser.evaluateExpression(stringBuffer.toString());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
