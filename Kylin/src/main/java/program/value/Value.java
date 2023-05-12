package program.value;

public class Value {
    private String name;
    private Object content;
    private boolean IsPublic = true;
    private String type;

    public void setName(String name) {
        this.name = name;
    }
    public void setContent(Object obj) {
        this.content = obj;
    }

    public String getName() {
        return this.name;
    }

    public void setType(String type)
    {
        this.type = type;
    }
    public String getType() {
        return this.type;
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
