package com.fordogs.configuraion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fordogs.configuraion.swagger.ApiErrorCode;
import com.fordogs.configuraion.swagger.ExampleHolder;
import com.fordogs.core.exception.error.BaseErrorCode;
import com.fordogs.core.presentation.ErrorResponse;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.jackson.TypeNameResolver;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Configuration
public class SwaggerConfiguration {

    private static final String AUTHORIZATION_HEADER_SCHEME = "AUTHORIZATION_HEADER";
    private static final String AUTHORIZATION_HEADER_BEARER = "BEARER";

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion,
                                 @Value("${spring.application.name}") String appName,
                                 @Value("${server.domain}") String serverDomain) {

        Info info = createApiInfo(appVersion, appName);
        List<Server> servers = createServers(serverDomain);
        SecurityRequirement securityRequirement = createSecurityRequirement();
        Components components = createComponents();

        return new OpenAPI()
                .info(info)
                .servers(servers)
                .addSecurityItem(securityRequirement)
                .components(components);
    }

    private Info createApiInfo(String appVersion, String appName) {
        return new Info()
                .title(appName + " API for Rio")
                .version(appVersion);
    }

    private List<Server> createServers(String serverDomain) {
        Server localServer = new Server()
                .url("https://localhost");
        Server prodServer = new Server()
                .url("https://" + serverDomain);

        return List.of(prodServer, localServer);
    }

    private SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement()
                .addList(AUTHORIZATION_HEADER_SCHEME);
    }

    private Components createComponents() {
        return new Components()
                .addSecuritySchemes(AUTHORIZATION_HEADER_SCHEME, new SecurityScheme()
                        .name(AUTHORIZATION_HEADER_SCHEME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(AUTHORIZATION_HEADER_BEARER)
                        .in(SecurityScheme.In.HEADER)
                        .bearerFormat(HttpHeaders.AUTHORIZATION));
    }

    @Bean
    public TypeNameResolver configureTypeNameResolution() {
        TypeNameResolver innerClassAwareTypeNameResolver = new TypeNameResolver() {
            @Override
            public String getNameOfClass(Class<?> cls) {
                String fullName = cls.getName();
                int lastDotIndex = fullName.lastIndexOf(".");
                return fullName.substring(lastDotIndex + 1).replace("$", ".");
            }
        };
        ModelConverters.getInstance()
                .addConverter(new ModelResolver(new ObjectMapper(), innerClassAwareTypeNameResolver));

        return innerClassAwareTypeNameResolver;
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            ApiErrorCode apiErrorCode = handlerMethod.getMethodAnnotation(ApiErrorCode.class);

            if (apiErrorCode != null) {
                generateErrorCodeResponseExample(operation, apiErrorCode.value());
            }

            return operation;
        };
    }

    private void generateErrorCodeResponseExample(Operation operation, Class<? extends BaseErrorCode>[] types) {
        ApiResponses responses = operation.getResponses();
        List<ExampleHolder> exampleHolders = new ArrayList<>();
        for (Class<? extends BaseErrorCode> type : types) {
            BaseErrorCode[] errorCodes = type.getEnumConstants();
            Arrays.stream(errorCodes)
                    .map(baseErrorCode -> generateErrorExampleHolder(operation, baseErrorCode))
                    .forEach(exampleHolders::add);
        }
        addExamplesToResponses(responses, exampleHolders.stream().collect(groupingBy(ExampleHolder::getCode)));
    }

    private ExampleHolder generateErrorExampleHolder(Operation operation, BaseErrorCode errorCode) {
        return ExampleHolder.builder()
                .holder(getSwaggerExample(operation, errorCode))
                .code(errorCode.getHttpStatus().value())
                .name(errorCode.name())
                .build();
    }

    private Example getSwaggerExample(Operation operation, BaseErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.of(generateUrlPath(operation), errorCode);
        Example example = new Example();
        example.setValue(errorResponse);

        return example;
    }

    private void addExamplesToResponses(ApiResponses responses, Map<Integer, List<ExampleHolder>> statusExampleHoldersMap) {
        statusExampleHoldersMap.forEach((status, exampleHolders) -> {
            Content content = new Content();
            MediaType mediaType = new MediaType();
            ApiResponse apiResponse = new ApiResponse();

            exampleHolders.forEach(exampleHolder -> {
                mediaType.addExamples(exampleHolder.getName(), exampleHolder.getHolder());
                content.addMediaType("application/json", mediaType);
            });

            apiResponse.setContent(content);
            responses.addApiResponse(status.toString(), apiResponse);
        });
    }

    private String generateUrlPath(Operation operation) {
        String urlPath = operation.getOperationId();
        StringBuilder queryString = new StringBuilder();

        if (operation.getParameters() != null) {
            for (Parameter parameter : operation.getParameters()) {
                if (parameter.getIn().equals(ParameterIn.PATH.toString())) {
                    String pathVariable = parameter.getExample() != null ? parameter.getExample().toString() : "";
                    urlPath = urlPath.replace("{" + parameter.getName() + "}", pathVariable);
                }
                if (parameter.getIn().equals(ParameterIn.QUERY.toString())) {
                    if (queryString.length() > 0) {
                        queryString.append("&");
                    }
                    queryString.append(parameter.getName()).append("=")
                            .append((parameter.getExample() != null) ? parameter.getExample().toString() : "");
                }
            }
        }

        return urlPath + (queryString.length() > 0 ? "?" + queryString : "");
    }
}
