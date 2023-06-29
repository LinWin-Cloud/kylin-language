package Program;

public class KylinBoolean {
    public boolean isBool(String str , KylinRuntime kylinRuntime)
    {
        try {
            if (str.contains("==")) {
                StringBuffer stringBuffer = new StringBuffer();
                String[] array = str.split("==");
                String a = array[0];
                String b = array[1];
                if (a.contains("(") && a.contains(")")) {
                    KylinFunction f = kylinRuntime.FunctionMap.get(a.substring(0,a.indexOf("(")));
                    if (f != null) {
                        String[] in = a.substring(a.indexOf("(")+1, a.lastIndexOf(")")).split(",\\s*");
                        f.setInput(in);
                        f.kylinRuntime.run();
                        stringBuffer.append(f.kylinRuntime.getResult());
                    }else {
                        return false;
                    }
                }
                if (b.contains("(") && b.contains(")")) {
                    KylinFunction f = kylinRuntime.FunctionMap.get(b.substring(0,b.indexOf("(")));
                    if (f != null) {
                        String[] in = a.substring(b.indexOf("(")+1, b.lastIndexOf(")")).split(",\\s*");
                        f.setInput(in);
                        f.kylinRuntime.run();
                        stringBuffer.append(f.kylinRuntime.getResult());
                    }else {
                        return false;
                    }
                }
                if (kylinRuntime.ValueMap.containsKey(a)) {
                    stringBuffer.append(kylinRuntime.ValueMap.get(a).getContent());
                }
                if (kylinRuntime.ValueMap.containsKey(b)) {
                    stringBuffer.append(kylinRuntime.ValueMap.get(b));
                }
                System.out.println(stringBuffer.toString());
                return Boolean.parseBoolean(stringBuffer.toString());
            }
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
