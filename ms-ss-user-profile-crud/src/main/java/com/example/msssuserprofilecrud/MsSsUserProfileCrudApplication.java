package com.example.msssuserprofilecrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MsSsUserProfileCrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsSsUserProfileCrudApplication.class, args);
    }

}
