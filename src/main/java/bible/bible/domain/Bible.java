package bible.bible.domain;

public class Bible {

    private int seq;
    private int cate;
    private int book;
    private int chapter;
    private int paragraph;
    private String sentence;
    private String testament;
    private String longLabel;
    private String shortLabel;

    // Getters and Setters
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getCate() {
        return cate;
    }

    public void setCate(int cate) {
        this.cate = cate;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getParagraph() {
        return paragraph;
    }

    public void setParagraph(int paragraph) {
        this.paragraph = paragraph;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getTestament() {
        return testament;
    }

    public void setTestament(String testament) {
        this.testament = testament;
    }

    public String getLongLabel() {
        return longLabel;
    }

    public void setLongLabel(String longLabel) {
        this.longLabel = longLabel;
    }

    public String getShortLabel() {
        return shortLabel;
    }

    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }
}
