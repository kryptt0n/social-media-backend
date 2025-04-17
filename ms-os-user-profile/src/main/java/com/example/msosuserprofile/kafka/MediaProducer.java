package com.example.msosuserprofile.kafka;

import com.example.msosuserprofile.dto.MediaRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaProducer {
    private final KafkaTemplate<String, MediaRequestDto> kafkaTemplate;

    @Value("${kafka.topic.media}")
    private String topic;

    public void send(MediaRequestDto payload) {
        kafkaTemplate.send(topic, payload.getSourceId(), payload);
        System.out.println("📤 Sent media payload to Kafka: " + payload);
    }

}
