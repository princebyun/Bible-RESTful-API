package bible.bible.service;

import bible.bible.domain.Bible;
import bible.bible.domain.BookInfo;
import bible.bible.mapper.BibleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BibleService {

    private final BibleMapper bibleMapper;

    public BibleService(BibleMapper bibleMapper) {
        this.bibleMapper = bibleMapper;
    }

    public List<Bible> getBibleVerses(Integer cate, Integer book, Integer chapter, Integer paragraph, String keyword) {
        return bibleMapper.findByFilter(cate, book, chapter, paragraph, keyword);
    }

    public List<String> getTestaments() {
        return bibleMapper.findDistinctTestaments();
    }

    public List<BookInfo> getBooks(Integer cate) {
        return bibleMapper.findDistinctBooks(cate);
    }

    public Integer getMaxChapter(Integer book) {
        if (book == null || book <= 0) {
            return 0;
        }
        return bibleMapper.findMaxChapterByBook(book);
    }
}
