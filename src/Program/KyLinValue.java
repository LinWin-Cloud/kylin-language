package Program;

public class KyLinValue {
    private String name;
    private String type;
    private String content;
    private boolean is_public = true;

    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setContent(String content , KyLinRuntime kylinRuntime) {
        this.content = new KyLinExpression().getExpression(content , kylinRuntime);
        this.type = KyLinType.getType(this.content , kylinRuntime);
    }
    public void setIs_public(boolean isPublic) {
        this.is_public = isPublic;
    }
    public String getContent() {
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
}
