package Program;

public class KylinType {
    public static String getType(String content) {
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
        return "string";
    }
}
