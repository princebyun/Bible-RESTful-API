package bible.bible.domain;

import java.util.List;

public class OllamaRequest {
    private String model;
    private List<Message> messages;
    private boolean stream;
    private String format;

    public OllamaRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
        this.stream = false;
        this.format = "json";
    }

    // Getter들 (JSON 변환을 위해 필수)
    public String getModel() {
        return model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public boolean isStream() {
        return stream;
    }

    public String getFormat() {
        return format;
    }
}