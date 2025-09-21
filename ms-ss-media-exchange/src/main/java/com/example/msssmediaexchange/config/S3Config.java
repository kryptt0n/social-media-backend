package com.example.msssmediaexchange.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3Config {
    @Value("${s3.access-key}")
    private String accessKey;

    @Value("${s3.secret-key}")
    private String secretKey;

    @Value("${s3.region}")
    private String region;

    @Value("${s3.internal-endpoint}")
    private String internalEndpoint;

    @Value("${s3.public-endpoint}")
    private String publicEndpoint;

    @Bean(name = "aws")
    public S3Client amazonS3() {
        return basicBuilder().build();
    }

    @Bean(name = "minio")
    public S3Client minioS3() {
        return  basicBuilder()
                .endpointOverride(URI.create(internalEndpoint))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true).build())
                .build();
    }

    @Bean
    S3Presigner presigner() {
        return S3Presigner.builder()
                .endpointOverride(URI.create(publicEndpoint))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(region))
                .build();
    }

    private S3ClientBuilder basicBuilder() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials));
    }
}
