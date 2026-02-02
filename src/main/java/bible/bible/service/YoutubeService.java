package bible.bible.service;

import bible.bible.domain.Video;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class YoutubeService {

    private static final String CHANNEL_URL = "https://www.youtube.com/@new_center/videos";

    public List<Video> getRecentVideos() throws IOException {
        List<Video> videos = new ArrayList<>();
        
        // 1. 채널 페이지에 접속하여 RSS 피드 URL 찾기
        Document channelPage = Jsoup.connect(CHANNEL_URL).get();
        Element rssLinkElement = channelPage.selectFirst("link[type='application/rss+xml']");
        
        if (rssLinkElement == null) {
            // RSS 링크를 찾지 못한 경우 빈 리스트 반환
            return videos;
        }
        String rssUrl = rssLinkElement.attr("href");

        // 2. RSS 피드 파싱
        Document feed = Jsoup.connect(rssUrl).get();
        Elements entries = feed.select("entry");

        int count = 0;
        for (Element entry : entries) {
            if (count >= 20) {
                break;
            }

            String title = entry.select("title").first().text();
            String link = entry.select("link").first().attr("href");
            
            // 썸네일 URL (media:thumbnail)
            Element thumbnailElement = entry.select("media|thumbnail").first();
            String thumbnailUrl = (thumbnailElement != null) ? thumbnailElement.attr("url") : "";

            // 발행일 (published)
            String published = entry.select("published").first().text();
            ZonedDateTime zdt = ZonedDateTime.parse(published);
            String formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(zdt);

            videos.add(new Video(title, link, thumbnailUrl, formattedDate));
            count++;
        }

        return videos;
    }
}
