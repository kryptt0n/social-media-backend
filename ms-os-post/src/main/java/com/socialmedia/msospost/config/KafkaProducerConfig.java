package com.socialmedia.msospost.config;

import com.socialmedia.msospost.dto.MediaPayload;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, MediaPayload> kafkaTemplate(ProducerFactory<String, MediaPayload> pf) {
        KafkaTemplate<String, MediaPayload> template = new KafkaTemplate<>(pf);
        template.setMessageConverter(new StringJsonMessageConverter()); // 💥 disables __TypeId__
        return template;
    }
}
