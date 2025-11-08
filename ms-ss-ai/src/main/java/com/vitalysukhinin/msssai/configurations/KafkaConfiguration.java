package com.vitalysukhinin.msssai.configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
public class KafkaConfiguration {
    @Bean
    public NewTopic generateContentTopic() {
        return TopicBuilder
                .name("generate-ai-content")
                .build();
    }

    @Bean
    public NewTopic contentGeneratedTopic() {
        return TopicBuilder
                .name("ai-content-generated")
                .build();
    }
}
