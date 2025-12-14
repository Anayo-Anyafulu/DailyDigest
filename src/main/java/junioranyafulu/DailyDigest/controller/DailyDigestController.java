package junioranyafulu.DailyDigest.controller;

import junioranyafulu.DailyDigest.domain.Digest;
import junioranyafulu.DailyDigest.service.DailyDigestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/digest")
public class DailyDigestController {

    private final DailyDigestService dailyDigestService;

    @Autowired
    public DailyDigestController(DailyDigestService dailyDigestService) {
        this.dailyDigestService = dailyDigestService;
    }

    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Digest> getLatestDigest() {
        Digest digest = dailyDigestService.getLatestDigest();
        return ResponseEntity.ok(digest);
    }

    @GetMapping(value = "/latest/html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getLatestDigestHtml() {
        Digest digest = dailyDigestService.getLatestDigest();
        return ResponseEntity.ok(digest.getHtmlContent());
    }

    @GetMapping(value = "/date/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Digest> getDigestByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Digest digest = dailyDigestService.getDigestByDate(date);
        return ResponseEntity.ok(digest);
    }

    @GetMapping(value = "/date/{date}/html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getDigestByDateHtml(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Digest digest = dailyDigestService.getDigestByDate(date);
        return ResponseEntity.ok(digest.getHtmlContent());
    }

    @PostMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Digest> generateDigest() {
        Digest digest = dailyDigestService.generateDailyDigest();
        return ResponseEntity.ok(digest);
    }
}
