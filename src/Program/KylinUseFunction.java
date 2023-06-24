package Program;

import main.baseFunction;

public class KylinUseFunction {
    public static String[] KylinKeyWord ={
            "getTime",
            "input"
    };
    public static boolean isUseFunction(String expression) {
        try {
            String funcName = expression.substring(0,expression.indexOf("(")).trim();
            boolean is = false;
            for (String s : KylinKeyWord) {
                if (s.equals(funcName))
                {
                    is = true;
                    break;
                }
            }
            return is;
        }catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }
    public static String UseFunction(String expression ,KylinRuntime kylinRuntime) throws Exception {
        String funcName = expression.substring(0,expression.indexOf("(")).trim();
        String content = expression.substring(expression.indexOf("(")+1 , expression.lastIndexOf(")")).trim();
        String[] split = content.split(",\\s*");
        if (funcName.equals(KylinKeyWord[0])) {
            return baseFunction.getTime();
        }else if (funcName.equals(KylinKeyWord[1])) {
            return baseFunction.input(new KylinExpression().getExpression(split[0] , kylinRuntime));
        }
        else {
            return null;
        }
    }
}
