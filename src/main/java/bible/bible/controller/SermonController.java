package bible.bible.controller;

import bible.bible.domain.Video;
import bible.bible.service.YoutubeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class SermonController {

    private final YoutubeService youtubeService;

    public SermonController(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;
    }

    @GetMapping("/sermons")
    public String listSermons(Model model) {
        try {
            List<Video> videos = youtubeService.getRecentVideos();
            model.addAttribute("videos", videos);
            model.addAttribute("channelUrl", "https://www.youtube.com/@new_center/videos");
        } catch (IOException e) {
            // 에러 발생 시 모델에 에러 메시지 추가
            model.addAttribute("error", "주일말씀 목록을 가져오는 데 실패했습니다.");
            e.printStackTrace();
        }
        return "sermons";
    }
}
