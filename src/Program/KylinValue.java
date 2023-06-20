package Program;

public class KylinValue {
    private String name;
    private String type;
    private String content;
    public KylinRuntime kylinRuntime;

    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setContent(String content) {
        this.content = new KylinExpression().getExpression(content , kylinRuntime);
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
}
