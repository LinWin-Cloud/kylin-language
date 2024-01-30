package Program;

public class KyLinFunction {
    public String name;
    public String[] input;
    public boolean isPublic = true;
    public boolean isException = false;
    public String err_code;
    public KyLinRuntime kylinRuntime = new KyLinRuntime("");

    public KyLinFunction(String name) {
        this.name = name;
        kylinRuntime.name = this.name;
        kylinRuntime.isFunction = true;
    }

    public void setInput(String[] input) {
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
