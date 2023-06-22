package Program;

public class KylinProgramBaseFunction {
    public static boolean isProgramBaseFunction(String code , KylinRuntime kylinRuntime) {
        try {
            String function = code.substring(0 , code.indexOf("(")).trim();
            String input = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
            if (function.equals("out")) {
                System.out.println(new KylinExpression().getExpression(input , kylinRuntime));
            }
            return true;
        }catch (Exception exception) {
            return false;
        }
    }
    public static boolean isDefinedFunction(String code) {
        try {
            String function = code.substring(0 , code.indexOf("(")).trim();
            String input = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
            return true;
        }catch (Exception exception) {
            //exception.printStackTrace();
            return false;
        }
    }
    public static boolean isRealDefinedFunction(String code , KylinRuntime kylinRuntime) {
        try {
            String function = code.substring(0 , code.indexOf("(")).trim();
            return isDefinedFunction(code) && kylinRuntime.FunctionMap.containsKey(function);
        }catch (Exception exception) {
            return false;
        }
    }
}
