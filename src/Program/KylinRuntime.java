package Program;

import java.util.ArrayList;
import java.util.Map;

public class KylinRuntime {
    public ArrayList<String> code;
    public Map<String , >
    public boolean isFunction = false;
    public void exec(String code, int i) {
        String[] words = code.split(" ");
        if (words[0].equals("var")) {

        }
    }
    public void run() throws Exception {
        for (int i = 0 ; i < this.code.size() ;i++) {
            this.exec(this.code.get(i) , i);
        }
    }
}
