package Program;

import main.baseFunction;

public class KylinUseFunction {
    public static String[] KylinKeyWord ={
            "getTime"
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
            return false;
        }
    }
    public static String UseFunction(String expression) throws Exception {
        String funcName = expression.substring(0,expression.indexOf("(")).trim();
        String content = expression.substring(expression.indexOf("(")+1 , expression.lastIndexOf(")")).trim();
        if (funcName.equals(KylinKeyWord[0])) {
            return baseFunction.getTime();
        }else {
            return null;
        }
    }
}
