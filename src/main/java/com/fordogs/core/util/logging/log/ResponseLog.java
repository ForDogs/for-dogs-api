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
public class ResponseLog {

    private String requestId;
    private int statusCode;
    private Map<String, String> header;
    private String body;
    private boolean isError;
    private String createAt;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("requestId")
    public String getRequestId() {
        return requestId;
    }

    @DynamoDbAttribute("statusCode")
    public int getStatusCode() {
        return statusCode;
    }

    @DynamoDbAttribute("header")
    public Map<String, String> getHeader() {
        return header;
    }

    @DynamoDbAttribute("body")
    public String getBody() {
        return body;
    }

    @DynamoDbAttribute("isError")
    public boolean isError() {
        return isError;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("createAt")
    public String getCreateAt() {
        return createAt;
    }
}
