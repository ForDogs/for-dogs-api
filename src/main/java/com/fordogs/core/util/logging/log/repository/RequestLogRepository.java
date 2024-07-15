package com.fordogs.core.util.logging.log.repository;

import com.fordogs.core.util.logging.log.RequestLog;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.Instant;

@Repository
public class RequestLogRepository {

    private final DynamoDbTable<RequestLog> requestLogDynamoDbTable;

    public RequestLogRepository(DynamoDbClient dynamoDbClient) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        this.requestLogDynamoDbTable = enhancedClient.table(RequestLog.class.getSimpleName(), TableSchema.fromBean(RequestLog.class));
    }

    public void insert(RequestLog requestLog){
        requestLogDynamoDbTable.putItem(requestLog);
    }

    public RequestLog findBy(RequestLog requestLog){
        return requestLogDynamoDbTable.getItem(requestLog);
    }

    public RequestLog findById(String id){
        QueryConditional conditional = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(id)
                        .build()
        );
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(conditional)
                .limit(1)
                .build();

        return requestLogDynamoDbTable.query(queryRequest).items().stream()
                .findAny()
                .orElseGet(()-> null);
    }

    public RequestLog findByIdAndDate(String id, Instant date){
        Key key = Key.builder()
                .partitionValue(id)
                .sortValue(String.valueOf(date))
                .build();

        return requestLogDynamoDbTable.getItem(key);
    }

    public RequestLog update(RequestLog requestLog){
        return requestLogDynamoDbTable.updateItem(requestLog);
    }

    public void delete(RequestLog requestLog){
        requestLogDynamoDbTable.deleteItem(requestLog);
    }
}
