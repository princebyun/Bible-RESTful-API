package bible.bible.dto;

import bible.bible.domain.Bible;
import bible.bible.domain.BookInfo;
import java.util.List;

/**
 * 성경 보기 API 응답 DTO
 */
public class BibleViewDto {

    private List<Bible> verses;
    private List<String> testaments;
    private List<BookInfo> books;
    private int maxChapter;
    private Integer selectedCate;
    private Integer selectedBook;
    private Integer selectedChapter;
    private String selectedKeyword;

    public BibleViewDto() {
    }

    public BibleViewDto(List<Bible> verses, List<String> testaments, List<BookInfo> books,
                        int maxChapter, Integer selectedCate, Integer selectedBook,
                        Integer selectedChapter, String selectedKeyword) {
        this.verses = verses;
        this.testaments = testaments;
        this.books = books;
        this.maxChapter = maxChapter;
        this.selectedCate = selectedCate;
        this.selectedBook = selectedBook;
        this.selectedChapter = selectedChapter;
        this.selectedKeyword = selectedKeyword;
    }

    public List<Bible> getVerses() {
        return verses;
    }

    public void setVerses(List<Bible> verses) {
        this.verses = verses;
    }

    public List<String> getTestaments() {
        return testaments;
    }

    public void setTestaments(List<String> testaments) {
        this.testaments = testaments;
    }

    public List<BookInfo> getBooks() {
        return books;
    }

    public void setBooks(List<BookInfo> books) {
        this.books = books;
    }

    public int getMaxChapter() {
        return maxChapter;
    }

    public void setMaxChapter(int maxChapter) {
        this.maxChapter = maxChapter;
    }

    public Integer getSelectedCate() {
        return selectedCate;
    }

    public void setSelectedCate(Integer selectedCate) {
        this.selectedCate = selectedCate;
    }

    public Integer getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(Integer selectedBook) {
        this.selectedBook = selectedBook;
    }

    public Integer getSelectedChapter() {
        return selectedChapter;
    }

    public void setSelectedChapter(Integer selectedChapter) {
        this.selectedChapter = selectedChapter;
    }

    public String getSelectedKeyword() {
        return selectedKeyword;
    }

    public void setSelectedKeyword(String selectedKeyword) {
        this.selectedKeyword = selectedKeyword;
    }
}
