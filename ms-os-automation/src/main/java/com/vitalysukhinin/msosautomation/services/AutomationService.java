package com.vitalysukhinin.msosautomation.services;

import com.vitalysukhinin.msosautomation.dtos.CreatePostRequestDto;
import com.vitalysukhinin.msosautomation.feign.AIClient;
import com.vitalysukhinin.msosautomation.feign.PostClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AutomationService {

    private static final Logger log = LoggerFactory.getLogger(AutomationService.class);
    private AIClient aiClient;
    private PostClient postClient;
    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 5 * 60 * 1000, initialDelay = 2 * 60 * 1000)
    public void generatePost() {
        kafkaTemplate.send("generate-ai-content", UUID.randomUUID().toString());
    }

    @KafkaListener(id = "automation-generated", topics = "ai-content-generated")
    public void listenGeneratedContent(String content) {
        log.warn("Automation received - " + content);
        CreatePostRequestDto createPostRequestDto = CreatePostRequestDto.builder()
                .username("ivan")
                .content(content)
                .build();

        postClient.createPost(createPostRequestDto);
    }
}
