package program.value;

public class Value {
    private String name;
    private Object content;
    private boolean IsPublic = true;

    public void setName(String name) {
        this.name = name;
    }
    public void setContent(Object obj) {
        this.content = obj;
    }

    public String getName() {
        return this.name;
    }

    public Object getContent() {
        return this.content;
    }
    public void setPublic(boolean isPublic) {
        this.IsPublic = isPublic;
    }
    public boolean isPublic() {
        return this.IsPublic;
    }
}
