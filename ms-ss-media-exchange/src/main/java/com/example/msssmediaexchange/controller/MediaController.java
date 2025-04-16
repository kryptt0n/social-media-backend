package com.example.msssmediaexchange.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.msssmediaexchange.dto.MediaPayload;
import com.example.msssmediaexchange.dto.Provider;
import com.example.msssmediaexchange.entity.Media;
import com.example.msssmediaexchange.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping
    public ResponseEntity<String> upload(@RequestBody MediaPayload payload) {
        if (payload.getBase64Image() == null || payload.getBase64Image().isBlank()) {
            System.out.println("⚠️ Missing image payload.");
            return ResponseEntity.badRequest().body("Missing image payload.");
        }

        try {
            System.out.println("📤 Media payload base64: " + payload.getBase64Image().substring(0, 30) + "...");
            mediaService.processMedia(payload);
            return ResponseEntity.accepted().body("Media accepted for processing.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to process media: " + e.getMessage());
        }
    }

    /**
     * ✅ GET media metadata by sourceId and provider (used by orchestrators like ms-os-post)
     * Example: GET /media/123/POST
     */
    @GetMapping("/{sourceId}/{provider}")
    public Optional<Media> findBySourceIdAndProvider(@PathVariable String sourceId, @PathVariable Provider provider) {
        return mediaService.findBySourceIdAndProvider(sourceId, provider);
    }

    /**
     * ✅ POST to test the media processing logic (bypasses Kafka)
     * Example: POST /media/test
     * Body: { "sourceId": 123, "provider": "POST", "base64Image": "..." }
     */
    @PostMapping("/test")
    public void testKafkaPayload(@RequestBody MediaPayload payload) {
        mediaService.processMedia(payload);
    }
}
