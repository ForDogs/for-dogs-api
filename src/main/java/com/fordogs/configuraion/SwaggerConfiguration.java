package com.fordogs.configuraion;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion, @Value("${spring.application.name}") String serverName) {
        Info info = new Info()
            .title(serverName + " API for Rio")
            .version(appVersion);

        return new OpenAPI()
            .components(new Components())
            .info(info);
    }
}
