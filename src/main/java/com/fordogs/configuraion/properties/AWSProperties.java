package com.fordogs.configuraion.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "cloud.aws")
public class AWSProperties {

    private String region;
    private Credentials credentials;
    private S3 s3;
    private Dynamodb dynamodb;


    @Getter
    @Setter
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }

    @Getter
    @Setter
    public static class S3 {
        private String bucketName;
    }

    @Getter
    @Setter
    public static class Dynamodb {
        private String endpoint;
    }
}
