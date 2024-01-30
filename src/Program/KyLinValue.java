package Program;

public class KyLinValue {
    private String name;
    private String type = "";
    private Object content;
    private boolean is_public = true;

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
            return;
        }
        if (kylinRuntime.classMap.containsKey(this.type)) {
            this.content = content;
        }
        //this.content = new KyLinExpression().getExpression(String.valueOf(content) , kylinRuntime);
        this.content = content;
        this.type = KyLinType.getType(String.valueOf(this.content) , kylinRuntime);
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

    @Override
    public String toString() {
        return this.content.toString();
    }
}
