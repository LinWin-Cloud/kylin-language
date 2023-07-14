package Program;

public class KyLinBoolean {
    public boolean isBool(String str , KyLinRuntime kylinRuntime)
    {
        try {
            if (str.contains("==")) {
                StringBuffer stringBuffer = new StringBuffer();
                String[] array = str.split("==");
                String a = array[0].trim();
                String b = array[1].trim();
                if (a.contains("(") && a.contains(")")) {
                    KyLinFunction f = kylinRuntime.FunctionMap.get(a.substring(0,a.indexOf("(")));
                    if (f != null) {
                        String[] in = a.substring(a.indexOf("(")+1, a.lastIndexOf(")")).split(",\\s*");
                        f.setInput(in);
                        f.kylinRuntime.run();
                        a = f.kylinRuntime.getResult();
                    }else {
                        return false;
                    }
                }
                if (b.contains("(") && b.contains(")")) {
                    KyLinFunction f = kylinRuntime.FunctionMap.get(b.substring(0,b.indexOf("(")));
                    if (f != null) {
                        String[] in = a.substring(b.indexOf("(")+1, b.lastIndexOf(")")).split(",\\s*");
                        f.setInput(in);
                        f.kylinRuntime.run();
                        b = f.kylinRuntime.getResult();
                    }else {
                        return false;
                    }
                }
                if (kylinRuntime.ValueMap.containsKey(a)) {
                    a = String.valueOf(kylinRuntime.ValueMap.get(a).getContent());
                }
                if (kylinRuntime.ValueMap.containsKey(b)) {
                    b = String.valueOf(kylinRuntime.ValueMap.get(b).getContent());
                }
                return a.equals(b);
            }
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
            return false;
        }
    }
}
