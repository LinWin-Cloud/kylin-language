package program.value;

import com.kylin.Main;
import com.kylin.Runtime.MainRuntime;

import java.io.FilterOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExecFunction {
    private MainRuntime mainRuntime;
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
    public String RunFunction() {
        try
        {
            MainRuntime runtime = new MainRuntime(this.name);
            runtime.execFunctionHashMap = mainRuntime.execFunctionHashMap;
            runtime.ValueMap = mainRuntime.ValueMap;
            runtime.isFunction = true;
            runtime.code = new ArrayList<>(this.code);
            runtime.name = this.name;
            runtime.ValueMap = this.input;
            runtime.run();
            return runtime.result;
        }catch (Exception exception) {
            MainRuntime.sendRuntimeError(exception.getMessage(), mainRuntime.codeLine);
            return null;
        }
    }
}
