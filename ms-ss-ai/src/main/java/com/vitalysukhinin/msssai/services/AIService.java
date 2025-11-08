package com.vitalysukhinin.msssai.services;

import com.vitalysukhinin.msssai.dtos.GeneratedPostContent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class AIService {

    private static final Logger log = LoggerFactory.getLogger(AIService.class);
    private OllamaChatModel chatModel;
    private KafkaTemplate<String, String> kafkaTemplate;
    private final List<String> themes = List.of(
            "Weather in Toronto", "Popular books",
            "Interesting facts about programming", "Fast cars",
            "Healthy eating", "Sports"
    );

    public GeneratedPostContent generatePostContent() {

        String content = chatModel.call(getPostRequest());

        return new GeneratedPostContent(content);
    }

    @KafkaListener(id = "ai-generated", topics = "generate-ai-content")
    public void listenGenerate(String jobId) {
        log.warn("Received jobId: " + jobId);
        sendGeneratedContent();
    }

    private void sendGeneratedContent() {
        kafkaTemplate.send("ai-content-generated", generatePostContent().getContent());
    }

    private String getPostRequest() {
        Random random = new Random();
        String theme = themes.get(random.nextInt(0, themes.size()));
        String request = "Generate a text, that contains from 20 to 50 words about specified theme. Make text very friendly " +
                "and avoid using any negative or offensive words. DON'T WRITE NUMBER OF WORDS PLEASE. " +
                "Make it in a style like you are writing a post in a blog " +
                "here is the theme for your post: " + theme;
        return request;
    }
}
