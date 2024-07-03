package com.fordogs.configuraion.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "port-one")
public class PortOneProperties {

    private String apiKey;
    private String apiSecret;
}
