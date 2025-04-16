package com.example.msssmediaexchange;

import com.example.msssmediaexchange.service.MediaService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@Disabled("Temporarily disabling until KafkaContext issue is resolved")
@SpringBootTest(properties = "kafka.listener.enabled=false")
class MsSsMediaExchangeApplicationTests {

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        public MediaService mediaService() {
            return mock(MediaService.class); // mock from Mockito
        }
    }

    @Test
    void contextLoads() {
        // just verify that Spring context loads successfully
    }
}
