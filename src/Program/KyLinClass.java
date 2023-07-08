package Program;

import java.util.ArrayList;
import java.util.HashMap;

public class KyLinClass {
    public String name;
    private boolean isPublic = false;
    private KyLinRuntime kyLinRuntime;
    public HashMap<String , KyLinFunction> functionHashMap = new HashMap<>();
    public HashMap<String , KyLinValue> valueHashMap = new HashMap<>();
    public ArrayList<String> code = new ArrayList<>();

    public KyLinClass(String name)
    {
        this.name = name;
        kyLinRuntime = new KyLinRuntime(name);
    }

    public void init_class() throws Exception
    {
        for (int i = 0 ; i < this.code.size() ; i++)
        {

        }
    }
}
