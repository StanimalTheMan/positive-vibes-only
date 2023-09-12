package com.jiggycode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;

@Configuration
public class AWSConfig {

    @Value("${aws.access.key.id}")
    private String awsAccessKeyId;

    @Value("${aws.secret.key.id}")
    private String awsSecretKeyId;

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public ComprehendClient comprehendClient(){
        return ComprehendClient.builder()
                .region(Region.of(awsRegion)).credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
