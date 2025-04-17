package com.example.msosuserprofile.config;

import com.example.msosuserprofile.dto.MediaRequestDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public KafkaTemplate<String, MediaRequestDto> kafkaTemplate(ProducerFactory<String, MediaRequestDto> pf) {
        KafkaTemplate<String, MediaRequestDto> template = new KafkaTemplate<>(pf);
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }
}
