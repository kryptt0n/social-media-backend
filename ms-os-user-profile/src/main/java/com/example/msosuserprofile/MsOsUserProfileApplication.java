package com.example.msosuserprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsOsUserProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsOsUserProfileApplication.class, args);
    }

}
