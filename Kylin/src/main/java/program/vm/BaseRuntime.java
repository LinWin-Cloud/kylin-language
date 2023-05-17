package program.vm;

import com.kylin.Runtime.Expression;
import com.kylin.Runtime.ListExpression;
import com.kylin.Runtime.MainRuntime;

import java.io.*;
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
            if (code.startsWith("shell")) {
                String execCommand = Expression.getExString(subContent , line, mainRuntime);
                boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
                Process process;
                if (isWindows) {
                    process = Runtime.getRuntime().exec("cmd /c "+execCommand);
                }
                else {
                    process = Runtime.getRuntime().exec("/bin/sh -c "+execCommand);
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String GetLine;
                while ((GetLine = reader.readLine()) != null) {
                    System.out.println(GetLine);
                }
                reader.close();
                return String.valueOf(process.waitFor());
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
    public static String[] getFunctionInput(String code, int line, MainRuntime mainRuntime) {
        try{
            String getInput = code.substring(code.indexOf("(") + 1, code.lastIndexOf(")"));
            ListExpression listExpression = new ListExpression();
            return listExpression.get_list_expression(getInput,line , mainRuntime);
        }catch (Exception exception) {
            MainRuntime.sendSyntaxError(exception.getMessage() , line);
            return null;
        }
    }
}
