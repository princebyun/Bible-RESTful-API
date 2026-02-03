package bible.bible.controller;

import bible.bible.dto.SermonsDto;
import bible.bible.service.YoutubeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bible.bible.domain.Video;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sermons")
public class SermonController {

    private static final String CHANNEL_URL = "https://www.youtube.com/@new_center/videos";

    private final YoutubeService youtubeService;

    public SermonController(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;
    }

    @GetMapping
    public ResponseEntity<SermonsDto> listSermons() {
        try {
            List<Video> videos = youtubeService.getRecentVideos();
            SermonsDto dto = new SermonsDto();
            dto.setVideos(videos);
            dto.setChannelUrl(CHANNEL_URL);
            return ResponseEntity.ok(dto);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok(SermonsDto.error("주일말씀 목록을 가져오는 데 실패했습니다."));
        }
    }
}
