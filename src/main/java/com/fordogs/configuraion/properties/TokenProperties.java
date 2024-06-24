package com.fordogs.configuraion.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "token")
@Getter
@Setter
public class TokenProperties {

    private String secretKey;

    private int accessTokenExpirationMinutes;

    private int refreshTokenExpirationDays;
}
