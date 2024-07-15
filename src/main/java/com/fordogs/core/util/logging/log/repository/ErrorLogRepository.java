package com.fordogs.core.util.logging.log.repository;

import com.fordogs.core.util.logging.log.ErrorLog;
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
public class ErrorLogRepository {

    private final DynamoDbTable<ErrorLog> requestLogDynamoDbTable;

    public ErrorLogRepository(DynamoDbClient dynamoDbClient) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        this.requestLogDynamoDbTable = enhancedClient.table(ErrorLog.class.getSimpleName(), TableSchema.fromBean(ErrorLog.class));
    }

    public void insert(ErrorLog errorLog){
        requestLogDynamoDbTable.putItem(errorLog);
    }

    public ErrorLog findBy(ErrorLog errorLog){
        return requestLogDynamoDbTable.getItem(errorLog);
    }

    public ErrorLog findById(String id){
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

    public ErrorLog findByIdAndDate(String id, Instant date){
        Key key = Key.builder()
                .partitionValue(id)
                .sortValue(String.valueOf(date))
                .build();

        return requestLogDynamoDbTable.getItem(key);
    }

    public ErrorLog update(ErrorLog errorLog){
        return requestLogDynamoDbTable.updateItem(errorLog);
    }

    public void delete(ErrorLog errorLog){
        requestLogDynamoDbTable.deleteItem(errorLog);
    }
}
