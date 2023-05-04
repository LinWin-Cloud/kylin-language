package program.value;

public class Value {
    private String name;
    private Object content;

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
}
