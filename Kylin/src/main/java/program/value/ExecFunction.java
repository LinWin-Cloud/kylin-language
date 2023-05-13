package program.value;

import com.kylin.Main;
import com.kylin.Runtime.MainRuntime;

import java.util.List;

public class ExecFunction {
    private MainRuntime mainRuntime;
    private String name;
    private boolean Public = false;
    private String[] input;
    public List<String> code;
    public void setMainRuntime(MainRuntime mainRuntime)
    {
        this.mainRuntime = mainRuntime;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setInput(String[] input)
    {
        this.input = input;
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
    public String[] getInput() {
        return this.input;
    }
    public void setCode(List<String> code) {
        this.code = code;
    }
}
