package Program;

import main.baseFunction;
import main.mainApp;

public class KyLinFunction {
    public final String name;
    public String[] input;
    public boolean isPublic = true;
    public boolean isException = false;
    public String err_code;
    public final KyLinRuntime kylinRuntime = new KyLinRuntime("");
    public final String pointer;

    public KyLinFunction(String name) {
        this.name = name;
        kylinRuntime.name = this.name;
        kylinRuntime.isFunction = true;
        this.pointer = String.valueOf(baseFunction.getRandomLong());
        mainApp.all_kylin_function_pointer.put(this.pointer , this);
    }

    public void setInput(String[] input , KyLinRuntime runtime) {
        this.input = input;
        for (String s : input)
        {
            KyLinValue kylinValue = new KyLinValue();
            kylinValue.setName(s);
            kylinValue.setIs_public(true);
            this.kylinRuntime.ValueMap.put(s , kylinValue);
        }
    }
}
