package program.vm;

import com.kylin.Runtime.Expression;
import com.kylin.Runtime.MainRuntime;

public class BaseRuntime {
    public String run(String code , int line , MainRuntime mainRuntime) {
        try
        {
            if (code.startsWith("out")) {
                String print = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")"));
                print = Expression.getExString(print,line,mainRuntime);
                System.out.println(print);
                return "";
            }
            else {
                MainRuntime.sendRuntimeError("[ERR] "+code , line+1);
                System.exit(1);
                return null;
            }
        }
        catch (Exception exception) {
            MainRuntime.sendRuntimeError(exception.getMessage() , line+1);
            return null;
        }
    }
}
