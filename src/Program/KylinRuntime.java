package Program;

import java.util.*;
import java.util.Map;
import java.util.Set;

public class KylinRuntime {
    public ArrayList<String> code;
    public Map<String , KylinValue> ValueMap = new HashMap<>();
    public boolean isFunction = false;
    public void exec(String code, int i) {
        String[] words = code.split(" ");
        if (words[0].equals("var")) {
            String name = words[1];
            String content = code.substring(code.indexOf("=")+1).trim();
            KylinValue kylinValue = new KylinValue();
            kylinValue.setContent(content);
            kylinValue.setName(name);
            this.ValueMap.put(name , kylinValue);
        }
    }
    public void run() throws Exception {
        for (int i = 0 ; i < this.code.size() ;i++) {
            this.exec(this.code.get(i) , i);
        }
    }
}
