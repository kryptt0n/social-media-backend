package com.vitalysukhinin.msssai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsSsAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSsAiApplication.class, args);
	}

}
