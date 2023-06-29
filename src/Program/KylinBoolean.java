package Program;

import main.mainApp;

public class KylinBoolean {
    public boolean isBool(String str , KylinRuntime kylinRuntime)
    {
        try {
            if (str.contains("==")) {
                StringBuffer stringBuffer = new StringBuffer();
                String[] array = str.split("==");
                String a = array[0].trim();
                String b = array[1].trim();
                System.out.println(a+" "+b);
                if (a.contains("(") && a.contains(")")) {
                    KylinFunction f = kylinRuntime.FunctionMap.get(a.substring(0,a.indexOf("(")));
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
                    KylinFunction f = kylinRuntime.FunctionMap.get(b.substring(0,b.indexOf("(")));
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
                    a = kylinRuntime.ValueMap.get(a).getContent();
                }
                if (kylinRuntime.ValueMap.containsKey(b)) {
                    b = kylinRuntime.ValueMap.get(b).getContent();
                }
                stringBuffer.append(a);
                stringBuffer.append("==");
                stringBuffer.append(b);
                return (boolean) mainApp.scriptEngine.eval(stringBuffer.toString());
            }
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
