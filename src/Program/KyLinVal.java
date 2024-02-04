package Program;

import main.mainApp;

public class KyLinVal {
    public void Val(String code , KyLinRuntime kyLinRuntime) throws Exception {
        String name = code.substring(code.indexOf(" ")+1,code.indexOf("=")).trim();
        String val  = code.substring(code.indexOf("=")+1).trim();

        String address = KyLinUseFunction.getAddress(name , kyLinRuntime).getContent().toString();

        mainApp.all_kylin_value_pointer.compute(address, (existingValue, newValue) -> {
            KyLinValue kyLinValue = newValue;
            try {
                kyLinValue.setContent(new KyLinExpression().getExpression(val , kyLinRuntime) , kyLinRuntime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return kyLinValue;
        });
    }
}
