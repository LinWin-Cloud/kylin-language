package Program;

public class KylinFunction {
    public String name;
    public String[] input;
    public boolean isPublic = true;
    public KylinRuntime kylinRuntime = new KylinRuntime();

    public KylinFunction() {
        kylinRuntime.isFunction = true;
    }

    public void setInput(String[] input) {
        this.input = input;
        for (String s : input)
        {
            KylinValue kylinValue = new KylinValue();
            kylinValue.setName(s);
            this.kylinRuntime.ValueMap.put(s , kylinValue);
        }
    }
}
