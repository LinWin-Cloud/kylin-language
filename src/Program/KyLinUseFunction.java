package Program;

import main.baseFunction;

import java.nio.file.LinkPermission;

public class KyLinUseFunction {
    public static String[] KylinKeyWord ={
            "getTime",
            "input",
            "get_os",
            "get_path",
            "new"
    };
    public static boolean isUseFunction(String expression , KyLinRuntime kylinRuntime) {
        try {
            String funcName = expression.substring(0,expression.indexOf("(")).trim();
            boolean is = false;
            if (kylinRuntime.defined_func.containsKey(funcName)) {
                return true;
            }
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
    public static Object UseFunction(String expression , KyLinRuntime kylinRuntime) throws Exception {
        String funcName = expression.substring(0,expression.indexOf("(")).trim();
        String content = expression.substring(expression.indexOf("(")+1 , expression.lastIndexOf(")")).trim();
        String[] split = content.split(",\\s*");

        String keyword = kylinRuntime.defined_func.get(funcName);
        if (keyword == null) {
            keyword = "";
        }
        if (funcName.equals(KylinKeyWord[0]) || keyword.equals(KylinKeyWord[0])) {
            return baseFunction.getTime();
        }else if (funcName.equals(KylinKeyWord[1]) || keyword.equals(KylinKeyWord[1])) {
            return baseFunction.input(new KyLinExpression().getExpression(split[0] , kylinRuntime));
        }
        else if (funcName.equals(KylinKeyWord[2]) || keyword.equals(KylinKeyWord[2])) {
            return System.getProperty("os.name");
        }
        else if (funcName.equals(KylinKeyWord[3]) || keyword.equals(KylinKeyWord[3])) {
            return System.getProperty("user.dir");
        }
        else if (funcName.equals(KylinKeyWord[4]) || keyword.equals(KylinKeyWord[4])) {
            KyLinClass kyLinClass = kylinRuntime.classMap.get(content);
            if (kyLinClass == null) {
                throw new Exception(content+" is not a class.");
            }else {
                kyLinClass.run_init_();
            }
            return kyLinClass;
        }
        else {
            return null;
        }
    }
}
