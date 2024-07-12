package com.fordogs.configuraion;

import com.fordogs.configuraion.properties.AWSProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3Configuration {

    private final AWSProperties awsProperties;

    @Bean
    public S3Client createS3Client() {
        AwsCredentials credentials = AwsBasicCredentials.create(awsProperties.getCredentials().getAccessKey(), awsProperties.getCredentials().getSecretKey());

        return S3Client.builder()
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
