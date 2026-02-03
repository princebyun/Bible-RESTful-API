package bible.bible.service;

import bible.bible.dto.QtTodayDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class QtService {

    private static final String QT_URL = "https://sum.su.or.kr:8888/bible/today";

    public QtTodayDto getTodayQt() {
        QtTodayDto dto = new QtTodayDto();
        try {
            Document doc = Jsoup.connect(QT_URL).get();

            Element dateElement = doc.selectFirst("#dailybible_info");
            dto.setDate(dateElement != null ? dateElement.text() : "날짜 정보 없음");

            Element titleElement = doc.selectFirst(".bible_text");
            dto.setTitle(titleElement != null ? titleElement.text() : "제목 정보 없음");

            Element passageElement = doc.selectFirst(".bibleinfo_box");
            dto.setPassage(passageElement != null ? passageElement.text() : "본문 범위 정보 없음");

            List<String> verseList = new ArrayList<>();
            Elements verseItems = doc.select("#body_list > li");

            if (!verseItems.isEmpty()) {
                for (Element item : verseItems) {
                    Element numElement = item.selectFirst(".num");
                    Element infoElement = item.selectFirst(".info");
                    if (numElement != null && infoElement != null) {
                        String verseNumber = numElement.text();
                        String verseInfo = infoElement.text();
                        verseList.add("<strong>" + verseNumber + "</strong> " + verseInfo);
                    }
                }
            }

            if (verseList.isEmpty()) {
                verseList.add("본문 내용을 가져올 수 없습니다. 사이트 구조가 변경되었을 수 있습니다.");
            }
            dto.setVerses(verseList);

        } catch (IOException e) {
            e.printStackTrace();
            return QtTodayDto.error("오늘의 큐티 본문을 가져오는 데 실패했습니다: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return QtTodayDto.error("알 수 없는 오류가 발생했습니다: " + e.getMessage());
        }
        return dto;
    }
}
