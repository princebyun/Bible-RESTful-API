package bible.bible.dto;

import java.util.List;

/**
 * 오늘의 큐티 API 응답 DTO
 */
public class QtTodayDto {

    private String date;
    private String title;
    private String passage;
    private List<String> verses;
    private String error;

    public QtTodayDto() {
    }

    public static QtTodayDto error(String message) {
        QtTodayDto dto = new QtTodayDto();
        dto.setError(message);
        return dto;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassage() {
        return passage;
    }

    public void setPassage(String passage) {
        this.passage = passage;
    }

    public List<String> getVerses() {
        return verses;
    }

    public void setVerses(List<String> verses) {
        this.verses = verses;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
