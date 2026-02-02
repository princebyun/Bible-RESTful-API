package bible.bible.domain.Gemini;

import java.util.List;

public class GeminiRequest {
    private List<Content> contents;

    public GeminiRequest(String text) {
        this.contents = List.of(new Content(text));
    }

    public GeminiRequest() {
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public static class Content {
        private List<Part> parts;

        public Content() {
        }

        public Content(String text) {
            this.parts = List.of(new Part(text));
        }

        public Content(List<Part> parts) {
            this.parts = parts;
        }

        public List<Part> getParts() {
            return parts;
        }

        public void setParts(List<Part> parts) {
            this.parts = parts;
        }
    }

    public static class Part {
        private String text;
        private InlineData inlineData;

        public Part() {
        }

        // 텍스트용 생성자
        public Part(String text) {
            this.text = text;
        }

        // 이미지용 생성자
        public Part(InlineData inlineData) {
            this.inlineData = inlineData;
        }

        public String getText() {
            return text;
        }

        public InlineData getInlineData() {
            return inlineData;
        }
    }

    public static class InlineData {
        private String mimeType;
        private String data;

        public InlineData() {
        }

        public InlineData(String mimeType, String data) {
            this.mimeType = mimeType;
            this.data = data;
        }

        public String getMimeType() {
            return mimeType;
        }

        public String getData() {
            return data;
        }
    }
}