package com.fordogs.core.util.logging.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.Map;

@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class RequestLog {

    private String requestId;
    private String url;
    private String method;
    private Map<String, String> header;
    private String body;
    private String createAt;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("requestId")
    public String getRequestId() {
        return requestId;
    }

    @DynamoDbAttribute("url")
    public String getUrl() {
        return url;
    }

    @DynamoDbAttribute("method")
    public String getMethod() {
        return method;
    }

    @DynamoDbAttribute("header")
    public Map<String, String> getHeader() {
        return header;
    }

    @DynamoDbAttribute("body")
    public String getBody() {
        return body;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("createAt")
    public String getCreateAt() {
        return createAt;
    }
}
