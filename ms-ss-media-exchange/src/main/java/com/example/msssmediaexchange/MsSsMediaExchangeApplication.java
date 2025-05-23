package com.example.msssmediaexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsSsMediaExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsSsMediaExchangeApplication.class, args);
    }

}
