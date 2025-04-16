package com.example.msssmediaexchange.kafka;

import com.example.msssmediaexchange.dto.MediaPayload;
import org.springframework.stereotype.Component;

@Component
public class MediaConsumer {

    public void onMediaReady(MediaPayload media) {
        System.out.println("📥 Received media update: " + media);

        // Simulate processing logic
        if (media.getBase64Image() != null) {
            System.out.println("✅ Base64 length: " + media.getBase64Image().length());
            System.out.println("✅ Provider: " + media.getProvider());
        } else {
            System.out.println("❌ No image received.");
        }
    }
}
