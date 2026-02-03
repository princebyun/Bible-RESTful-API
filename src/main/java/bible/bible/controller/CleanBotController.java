package bible.bible.controller;

import bible.bible.domain.CleanBotResult;
import bible.bible.service.CleanBotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/cleanbot")
public class CleanBotController {

    private final CleanBotService cleanBotService;

    public CleanBotController(CleanBotService cleanBotService) {
        this.cleanBotService = cleanBotService;
    }

    @PostMapping("/check")
    public ResponseEntity<CleanBotResult> checkComment(@RequestBody CommentRequest request) {
        CleanBotResult result = cleanBotService.checkContent(request.getText());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/check-image")
    public ResponseEntity<CleanBotResult> checkImage(@RequestParam("file") MultipartFile file) {
        CleanBotResult result = cleanBotService.checkImage(file);
        return ResponseEntity.ok(result);
    }

    public static class CommentRequest {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
