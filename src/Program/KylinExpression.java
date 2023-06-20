package Program;

public class KylinExpression {
    public String getExpression(String code) {
        try {
            String[] tokens = code.split("[<>]");
            StringBuffer stringBuffer = new StringBuffer();
            for (String s : tokens)
            {
                System.out.println(s);
            }
            return stringBuffer.toString();
        }
        catch (Exception exception) {
            System.out.println("[ERR] "+exception.getMessage());
            System.exit(1);
            return null;
        }
    }
}
