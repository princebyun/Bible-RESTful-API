package bible.bible.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QtController {

    @GetMapping("/qt")
    public String getQtPage(Model model) {
        String url = "https://sum.su.or.kr:8888/bible/today";
        try {
            Document doc = Jsoup.connect(url).get();

            // 날짜 가져오기 (Null 체크)
            Element dateElement = doc.selectFirst("#dailybible_info");
            String date = (dateElement != null) ? dateElement.text() : "날짜 정보 없음";

            // 제목 가져오기 (Null 체크)
            Element titleElement = doc.selectFirst(".bible_text");
            String title = (titleElement != null) ? titleElement.text() : "제목 정보 없음";

            // 본문 범위 가져오기 (Null 체크)
            Element passageElement = doc.selectFirst(".bibleinfo_box");
            String passage = (passageElement != null) ? passageElement.text() : "본문 범위 정보 없음";

            // --- 본문 파싱 로직 수정 ---
            List<String> verseList = new ArrayList<>();
            Elements verseItems = doc.select("#body_list > li"); // 1. ul > li 선택

            if (!verseItems.isEmpty()) {
                for (Element item : verseItems) {
                    // 2. 각 li 안에서 .num과 .info를 찾음
                    Element numElement = item.selectFirst(".num");
                    Element infoElement = item.selectFirst(".info");

                    // 3. Null 체크 후 절 번호와 내용을 조합
                    if (numElement != null && infoElement != null) {
                        String verseNumber = numElement.text();
                        String verseInfo = infoElement.text();
                        // JSP에서 바로 출력할 수 있도록 HTML 문자열로 만듦
                        verseList.add("<strong>" + verseNumber + "</strong> " + verseInfo);
                    }
                }
            }

            // 본문을 가져오지 못했을 경우의 예외 처리
            if (verseList.isEmpty()) {
                verseList.add("본문 내용을 가져올 수 없습니다. 사이트 구조가 변경되었을 수 있습니다.");
            }

            model.addAttribute("date", date);
            model.addAttribute("title", title);
            model.addAttribute("passage", passage);
            model.addAttribute("verses", verseList);

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "오늘의 큐티 본문을 가져오는 데 실패했습니다: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "알 수 없는 오류가 발생했습니다: " + e.getMessage());
        }
        return "qt";
    }
}
