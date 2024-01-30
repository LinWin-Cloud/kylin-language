package Function;

import Program.KyLinRuntime;
import Program.KyLinValue;

public class TypeOf {
    public static String typeOf(String value,KyLinRuntime kyLinRuntime) throws Exception {
        KyLinValue value_1 = kyLinRuntime.ValueMap.get(value);
        KyLinValue value_2 = null;
        if (kyLinRuntime.PublicRuntime != null) {
            value_2 = kyLinRuntime.PublicRuntime.ValueMap.get(value);
        }

        if (value_1 != null) {
            return value_1.getType();
        }
        else if (value_2 != null) {
            return value_2.getType();
        }else {
            throw new Exception("No Value: "+value);
        }
    }
}
