package bible.bible.controller;

import bible.bible.service.BibleService;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BibleController {

    private final BibleService bibleService;

    public BibleController(BibleService bibleService) {
        this.bibleService = bibleService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/bible")
    public String bible(
            @RequestParam Optional<Integer> cate,
            @RequestParam Optional<Integer> book,
            @RequestParam Optional<Integer> chapter,
            @RequestParam Optional<String> keyword,
            Model model) {

        boolean isInitialLoad = !cate.isPresent() && !book.isPresent() && !chapter.isPresent() && !keyword.isPresent();

        Integer finalCate;
        Integer finalBook;
        Integer finalChapter;
        String finalKeyword;

        if (isInitialLoad) {
            finalCate = 1;
            finalBook = 1;
            finalChapter = 1; // 초기값을 1(1장)로 변경
            finalKeyword = null;
        } else {
            finalCate = cate.filter(c -> c > 0).orElse(null);
            finalBook = book.filter(b -> b > 0).orElse(null);
            finalChapter = chapter.filter(c -> c > 0).orElse(null);
            finalKeyword = keyword.filter(k -> !k.trim().isEmpty()).orElse(null);
        }

        model.addAttribute("verses",
                bibleService.getBibleVerses(finalCate, finalBook, finalChapter, null, finalKeyword));
        model.addAttribute("testaments", bibleService.getTestaments());
        model.addAttribute("books", bibleService.getBooks(finalCate));

        // 선택된 책의 최대 장 수를 모델에 추가 (JSP에서 장 드롭다운을 만들기 위해)
        if (finalBook != null) {
            model.addAttribute("maxChapter", bibleService.getMaxChapter(finalBook));
        } else {
            model.addAttribute("maxChapter", 0);
        }

        model.addAttribute("selectedCate", finalCate);
        model.addAttribute("selectedBook", finalBook);
        model.addAttribute("selectedChapter", finalChapter);
        model.addAttribute("selectedKeyword", finalKeyword);

        return "view";
    }

    @GetMapping("/api/chapters")
    @ResponseBody
    public Map<String, Integer> getChapters(@RequestParam Integer book) {
        Integer maxChapter = bibleService.getMaxChapter(book);
        return Collections.singletonMap("maxChapter", maxChapter);
    }
}
