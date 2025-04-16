package com.example.msssmediaexchange.controller;

import com.example.msssmediaexchange.dto.MediaPayload;
import com.example.msssmediaexchange.kafka.MediaConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-media")
@RequiredArgsConstructor
public class MediaTestController {

    private final MediaConsumer mediaConsumer;

    @PostMapping
    public String simulateKafka(@RequestBody MediaPayload payload) {
        mediaConsumer.onMediaReady(payload);
        return "✅ Received and processed manually.";
    }
}
