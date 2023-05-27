package program.Function;

import com.kylin.Runtime.MainRuntime;

public class BaseFunction {
    public static boolean isBaseFunction(String name , String code, MainRuntime mainRuntime)
    {
        if (name.equals("ky.version")) {
            return true;
        }
        if (name.equals("getTime")){
            return true;
        }
        else {
            return false;
        }
    }
    public static String getBaseFunctionResult(String name, String code , MainRuntime mainRuntime)
    {
        if (name.equals("ky.version"))
        {
            return "kylin1.1";
        }
        if (name.equals("getTime")) {
            return String.valueOf(System.currentTimeMillis());
        }
        else {
            return "";
        }
    }
}
