package program.vm;

import com.kylin.Runtime.MainRuntime;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;

public class BaseLib {
    public static void javascript(ArrayList<String> code , int codeLine,MainRuntime mainRuntime) {
        try {
            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");
            List<String> stringList = new ArrayList<>();

            for (int i = codeLine + 1; i < code.size() ;i++)
            {
                String js = code.get(i).trim();
                if (js.equals("end_js")) {
                    mainRuntime.codeLine = i;
                    break;
                }
                stringList.add(js);
            }
            for (int i = 0 ; i < stringList.size() ;i++)
            {
                String js = stringList.get(i).trim();
                if (js.equals("exit")) {
                    break;
                }
                Object obj = scriptEngine.eval(js);
                if (obj != null) {
                    System.out.println(obj.toString());
                }
            }
        }
        catch (Exception exception){
            MainRuntime.sendRuntimeError("Javascript Engine: "+exception.getMessage(),codeLine);
        }
    }
}
