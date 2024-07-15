package com.fordogs.core.util.logging.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class ErrorLog {

    private String requestId;
    private String userId;
    private String path;
    private int statusCode;
    private String message;
    private String stackTrace;
    private String timeStamp;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("requestId")
    public String getRequestId() {
        return requestId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "UserIdIndex")
    @DynamoDbAttribute("userId")
    public String getUserId() {
        return userId;
    }

    @DynamoDbAttribute("path")
    public String getPath() {
        return path;
    }

    @DynamoDbAttribute("statusCode")
    public int getStatusCode() {
        return statusCode;
    }

    @DynamoDbAttribute("message")
    public String getMessage() {
        return message;
    }

    @DynamoDbAttribute("stackTrace")
    public String getStackTrace() {
        return stackTrace;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("timeStamp")
    public String getTimeStamp() {
        return timeStamp;
    }
}
