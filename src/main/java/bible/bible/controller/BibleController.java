package bible.bible.controller;

import bible.bible.dto.BibleViewDto;
import bible.bible.service.BibleService;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bible")
public class BibleController {

    private final BibleService bibleService;

    public BibleController(BibleService bibleService) {
        this.bibleService = bibleService;
    }

    /**
     * 성경 보기 데이터 (구절 목록, 구약/신약, 책 목록, 최대 장, 선택값)
     */
    @GetMapping("/view")
    public ResponseEntity<BibleViewDto> getBibleView(
            @RequestParam Optional<Integer> cate,
            @RequestParam Optional<Integer> book,
            @RequestParam Optional<Integer> chapter,
            @RequestParam Optional<String> keyword) {

        boolean isInitialLoad = !cate.isPresent() && !book.isPresent() && !chapter.isPresent() && !keyword.isPresent();

        Integer finalCate;
        Integer finalBook;
        Integer finalChapter;
        String finalKeyword;

        if (isInitialLoad) {
            finalCate = 1;
            finalBook = 1;
            finalChapter = 1;
            finalKeyword = null;
        } else {
            finalCate = cate.filter(c -> c > 0).orElse(null);
            finalBook = book.filter(b -> b > 0).orElse(null);
            finalChapter = chapter.filter(c -> c > 0).orElse(null);
            finalKeyword = keyword.filter(k -> !k.trim().isEmpty()).orElse(null);
        }

        var verses = bibleService.getBibleVerses(finalCate, finalBook, finalChapter, null, finalKeyword);
        var testaments = bibleService.getTestaments();
        var books = bibleService.getBooks(finalCate);
        int maxChapter = (finalBook != null) ? bibleService.getMaxChapter(finalBook) : 0;

        BibleViewDto dto = new BibleViewDto(
                verses, testaments, books, maxChapter,
                finalCate, finalBook, finalChapter, finalKeyword);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/chapters")
    public ResponseEntity<Map<String, Integer>> getChapters(@RequestParam Integer book) {
        Integer maxChapter = bibleService.getMaxChapter(book);
        return ResponseEntity.ok(Collections.singletonMap("maxChapter", maxChapter));
    }
}
