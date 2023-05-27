package HttpService.HTML;

public class VirtualWebObject {
    private String content;
    private String content_type;

    public void setContent(String content) {
        this.content = content;
    }
    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getContent() {
        return this.content;
    }
    public String getContent_type() {
        return this.content_type;
    }
}
