package Program;

public class KyLinType {
    public static String getType(String content, KyLinRuntime kylinRuntime) {
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
        if (content.equalsIgnoreCase("true") || content.equalsIgnoreCase("false")) {
            return "bool";
        }
        return "string";
    }
}
