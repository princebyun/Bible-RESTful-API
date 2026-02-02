package bible.bible.controller;

import bible.bible.domain.CleanBotResult;
import bible.bible.service.CleanBotService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CleanBotController {

    private final CleanBotService cleanBotService;

    public CleanBotController(CleanBotService cleanBotService) {
        this.cleanBotService = cleanBotService;
    }

    // --- 화면 이동 --

    @GetMapping("/cleanbot/chat")
    public String chatPage() {
        return "cleanbot-chat";
    }

    @GetMapping("/cleanbot/image")
    public String imagePage() {
        return "cleanbot-image";
    }

    // --- API ---

    @PostMapping("/api/cleanbot/check")
    @ResponseBody
    public ResponseEntity<CleanBotResult> checkComment(@RequestBody CommentRequest request) {
        CleanBotResult result = cleanBotService.checkContent(request.getText());
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/api/cleanbot/check-image")
    @ResponseBody
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
