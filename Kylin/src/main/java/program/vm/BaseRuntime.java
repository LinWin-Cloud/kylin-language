package program.vm;

import com.kylin.Runtime.Expression;
import com.kylin.Runtime.ListExpression;
import com.kylin.Runtime.MainRuntime;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class BaseRuntime {
    public String run(String code , int line , MainRuntime mainRuntime) {
        try
        {
            String subContent = code.substring(code.indexOf("(") + 1, code.lastIndexOf(")"));
            if (code.startsWith("out")) {
                String print = subContent;
                print = Expression.getExString(print,line,mainRuntime);
                System.out.println(print);
                return "";
            }
            if (code.startsWith("eval_js")) {
                String print = subContent;
                print = Expression.getExString(print,line,mainRuntime);
                String[] s = {print};
                BaseLib.javascript(new ArrayList<>(Arrays.asList(s)) , line, mainRuntime);
                return "";
            }
            if (code.startsWith("write")) {
                String getInput = subContent;
                ListExpression listExpression = new ListExpression();
                String[] inputContent = listExpression.get_list_expression(getInput,line , mainRuntime);
                try{
                    String path = inputContent[1];

                }catch (Exception exception) {
                    MainRuntime.sendSyntaxError(exception.getMessage() , line);
                }
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
    public static String[] getFunctionInput(String code, int line) {
        String getInput = code.substring(code.indexOf("(") + 1, code.lastIndexOf(")"));
        ListExpression listExpression = new ListExpression();
        String[] inputContent = listExpression.get_list_expression(getInput,line , mainRuntime);
    }
}
