package bible.bible.domain;

public class Video {
    private String title;
    private String link;
    private String thumbnailUrl;
    private String publishedDate;

    public Video(String title, String link, String thumbnailUrl, String publishedDate) {
        this.title = title;
        this.link = link;
        this.thumbnailUrl = thumbnailUrl;
        this.publishedDate = publishedDate;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }
}
