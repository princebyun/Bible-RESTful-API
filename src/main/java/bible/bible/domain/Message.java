package bible.bible.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    private String role;
    private String content;
    private List<String> images;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public Message(String role, String content, List<String> images) {
        this.role = role;
        this.content = content;
        this.images = images;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
