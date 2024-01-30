package Program;

public class KyLinVal {
    public void Val(String code , KyLinRuntime kyLinRuntime) throws Exception {
        String name = code.substring(code.indexOf(" ")+1,code.indexOf("=")).trim();
        String val  = code.substring(code.indexOf("=")+1).trim();

        if (kyLinRuntime.ValueMap.containsKey(name)) {
            kyLinRuntime.ValueMap.get(name).setContent(new KyLinExpression().getExpression(val,kyLinRuntime) , kyLinRuntime);
        }else if (kyLinRuntime.PublicRuntime != null && kyLinRuntime.PublicRuntime.ValueMap.containsKey(name)) {
            kyLinRuntime.PublicRuntime.ValueMap.get(name).setContent(new KyLinExpression().getExpression(val,kyLinRuntime) , kyLinRuntime);
        }
        else {
            throw new Exception("Target value error: "+name);
        }
    }
}
