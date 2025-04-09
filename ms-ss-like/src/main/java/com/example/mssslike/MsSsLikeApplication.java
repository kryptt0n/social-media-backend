package com.example.mssslike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsSsLikeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsSsLikeApplication.class, args);
    }

}
