package org.peagadev.loadingps2024.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Log4j2
public class Config {

    @Value("${aws.credentials.ACCESS-KEY}")
    private String accessKey;
    @Value("${aws.credentials.SECRET-KEY}")
    private String secretKey;
    @Value("${aws.credentials.BUCKET-REGION}")
    private String bucketRegion;

    @Bean
    public S3Client getS3Client() {
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(bucketRegion)).build();
    }
}
