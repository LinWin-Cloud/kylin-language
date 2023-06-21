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
            kylinValue.kylinRuntime = this;
            kylinValue.setContent(content);
            kylinValue.setName(name);
            this.ValueMap.put(name , kylinValue);
        }
        if (words[0].equals("func")) {
            /**
             * func func_name (a ,b) public
             *      return <a + b>
             * end_func
             */
            String name = code.substring(code.indexOf(" ")+1,code.indexOf("(")).trim();
            String inputContent = code.substring(code.indexOf("(")+1 , code.lastIndexOf(")")).trim();
            
        }
    }
    public void run() throws Exception {
        for (int i = 0 ; i < this.code.size() ;i++) {
            this.exec(this.code.get(i) , i);
        }
    }
}
