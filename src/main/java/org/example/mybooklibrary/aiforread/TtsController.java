package org.example.mybooklibrary.aiforread;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/tts")
@RequiredArgsConstructor
public class TtsController {

    private final OpenTtsService ttsService;

    @PostMapping("/speak")
    public ResponseEntity<String> speak(@RequestBody TtsRequest req) {
        // (Optional) still guard against empty text manually:
        if (req.getText() == null || req.getText().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Text must not be empty");
        }

        String fileName = UUID.randomUUID().toString() + ".mp3";
        ttsService.generateAudio(req.getText(), req.getVoice(), fileName);

        return ResponseEntity.ok("/api/tts/audio/" + fileName);
    }

    @GetMapping("/audio/{fileName}")
    public ResponseEntity<Resource> getAudio(@PathVariable String fileName) throws IOException {
        File file = new File("src/main/resources/audio/" + fileName);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + file.getName())
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resource);
    }
}
