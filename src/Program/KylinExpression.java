package Program;

import java.util.Arrays;

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
                else if(KylinProgramBaseFunction.isDefinedFunction(code , kylinRuntime)) {
                    String function = code.substring(0 , code.indexOf("(")).trim();
                    String input = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
                    KylinFunction kylinFunction = kylinRuntime.FunctionMap.get(function);

                    String[] func_content = input.split(",\\s*");
                    for (int j = 0 ; j < kylinFunction.input.length ; j++) {
                        KylinValue kylinValue = new KylinValue();
                        kylinValue.setName(kylinFunction.input[j]);
                        kylinValue.setContent(func_content[j]);
                        kylinFunction.kylinRuntime.ValueMap.put(kylinFunction.input[j] , kylinValue);
                    }
                    kylinFunction.kylinRuntime.run();
                    stringBuffer.append(kylinFunction.kylinRuntime.getResult());
                    continue;
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
}
