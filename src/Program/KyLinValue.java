package Program;

import main.baseFunction;
import main.mainApp;

public class KyLinValue {
    private String name;
    private String type = "";
    private Object content;
    private boolean is_public = true;
    private long pointer;
    public boolean isNumber(String in) {
        try {
            Double.parseDouble(in);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setContent(Object content , KyLinRuntime kylinRuntime) {
        if (content == null) {
            this.content = null;
            this.type = "null";
            this.pointer = baseFunction.getRandomLong();
            mainApp.all_kylin_value_pointer.put(String.valueOf(this.pointer) , this);
            return;
        }
        if (kylinRuntime.classMap.containsKey(this.type)) {
            this.content = content;
        }
        if (isNumber(content.toString())) {
            this.type = "num";
        }
        this.content = content;
        this.pointer = baseFunction.getRandomLong();
        mainApp.all_kylin_value_pointer.put(String.valueOf(this.pointer) , this);
    }
    public KyLinValue() {

    }
    public void setPointer(long pointer) {
        this.pointer = pointer;
    }
    public void setIs_public(boolean isPublic) {
        this.is_public = isPublic;
    }
    public Object getContent() {
        return this.content;
    }
    public String getName() {
        return this.name;
    }
    public String getType() {
        return this.type;
    }
    public boolean isPublic() {
        return this.is_public;
    }
    public long getPointer() {
        return this.pointer;
    }

    @Override
    public String toString() {
        return this.content.toString();
    }
}
