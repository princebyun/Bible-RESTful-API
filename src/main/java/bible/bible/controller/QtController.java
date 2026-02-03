package bible.bible.controller;

import bible.bible.dto.QtTodayDto;
import bible.bible.service.QtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qt")
public class QtController {

    private final QtService qtService;

    public QtController(QtService qtService) {
        this.qtService = qtService;
    }

    @GetMapping("/today")
    public ResponseEntity<QtTodayDto> getTodayQt() {
        return ResponseEntity.ok(qtService.getTodayQt());
    }
}
