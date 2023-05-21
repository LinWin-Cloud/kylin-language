package program.value;

import com.kylin.Main;
import com.kylin.Runtime.MainRuntime;

import java.io.FilterOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExecFunction {
    public MainRuntime mainRuntime;
    private String name;
    public String lastRuntime = null;
    private boolean Public = false;
    public String[] inputList;
    public HashMap<String,Value> input = new HashMap<>();
    public List<String> code;
    public void setMainRuntime(MainRuntime mainRuntime)
    {
        this.mainRuntime = mainRuntime;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPublic(boolean isPublic) {
        this.Public = isPublic;
    }
    public String getName() {
        return this.name;
    }
    public MainRuntime getMainRuntime() {
        return this.mainRuntime;
    }
    public boolean isPublic() {
        return this.Public;
    }
    public void setCode(List<String> code) {
        this.code = code;
    }

    public MainRuntime runtime;

    public ExecFunction()
    {
        runtime = new MainRuntime(null);
    }
    public String RunFunction() throws Exception {
        try
        {
            runtime.name = name;
            runtime.isFunction = true;
            runtime.code = new ArrayList<>(this.code);
            runtime.name = this.name;
            runtime.PublicRuntime = this.lastRuntime;
            runtime.ValueMap.putAll(input);
            runtime.ValueMap.putAll(mainRuntime.ValueMap);
            runtime.execFunctionHashMap.putAll(mainRuntime.execFunctionHashMap);
            runtime.run();
            return runtime.getResult();
        }catch (Exception exception) {
            exception.printStackTrace();
            MainRuntime.sendRuntimeError("ERR: "+exception.getMessage(), mainRuntime.codeLine);
            return null;
        }
    }
}
