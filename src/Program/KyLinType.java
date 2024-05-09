package Program;

import main.mainApp;


public class KyLinType {
    public static String getType(String content, KyLinRuntime kylinRuntime) {
        try {
            String address = KyLinUseFunction.getAddress(content , kylinRuntime).toString();
            if (mainApp.all_kylin_value_pointer.containsKey(address))
            {
                return mainApp.all_kylin_value_pointer.get(address).getType();
            }
        }catch (Exception ignored) {
        }
        try {
            double b = Double.parseDouble(content);
            return "num";
        }
        catch (Exception ignored) {
        }
        try {
            int i = Integer.parseInt(content);
            return "num";
        }
        catch (Exception ignored) {
        }
        if ("true".equalsIgnoreCase(content) || "false".equalsIgnoreCase(content)) {
            return "bool";
        }
        return "string";
    }
}
