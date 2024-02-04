package Program;


import java.util.ArrayList;

public class KyLinList {
    public String name;
    public ArrayList<KyLinValue> arrayList;

    @Override
    public String toString() {
        return arrayList.toString();
    }
}
