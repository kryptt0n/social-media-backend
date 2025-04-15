package com.socialmedia.msospost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.socialmedia.msospost.client")
public class MsOsPostApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsOsPostApplication.class, args);
    }

}
