package com.socialmedia.msospost.kafka;

import com.socialmedia.msospost.dto.MediaPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MediaProducer {

    private final KafkaTemplate<String, MediaPayload> kafkaTemplate;

    @Value("${kafka.topic.media}")
    private String topic;

    public void sendMediaPayload(MediaPayload payload) {
        kafkaTemplate.send(topic, payload.getSourceId(), payload);
        System.out.println("📤 Sent media payload to Kafka: " + payload);
    }
}
