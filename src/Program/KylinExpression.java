package Program;

public class KylinExpression {
    public String getExpression(String code , KylinRuntime kylinRuntime) {
        try {
            String[] tokens = code.split("[<>]");
            StringBuffer stringBuffer = new StringBuffer();
            for (String s : tokens)
            {
                s = s.trim();
                if (s.equals("")) {
                    continue;
                }
                if (s.startsWith("\"") && s.endsWith("\"")) {
                    stringBuffer.append(s, 1, s.length() - 1);
                    continue;
                }
                if (kylinRuntime.ValueMap.containsKey(s)) {
                    stringBuffer.append(kylinRuntime.ValueMap.get(s).getContent());
                    continue;
                }
            }
            //System.out.println(stringBuffer.toString());
            return stringBuffer.toString();
        }
        catch (Exception exception) {
            System.out.println("[ERR] "+exception.getMessage());
            System.exit(1);
            return null;
        }
    }
}
