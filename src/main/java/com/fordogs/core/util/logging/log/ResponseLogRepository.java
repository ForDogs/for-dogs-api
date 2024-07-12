package com.fordogs.core.util.logging.log;

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
public class ResponseLogRepository {

    private final DynamoDbTable<ResponseLog> responseLogDynamoDbTable;

    public ResponseLogRepository(DynamoDbClient dynamoDbClient) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        this.responseLogDynamoDbTable = enhancedClient.table(ResponseLog.class.getSimpleName(), TableSchema.fromBean(ResponseLog.class));
    }

    public void insert(ResponseLog responseLog){
        responseLogDynamoDbTable.putItem(responseLog);
    }

    public ResponseLog findBy(ResponseLog responseLog){
        return responseLogDynamoDbTable.getItem(responseLog);
    }

    public ResponseLog findById(String id){
        QueryConditional conditional = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(id)
                        .build()
        );
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(conditional)
                .limit(1)
                .build();

        return responseLogDynamoDbTable.query(queryRequest).items().stream()
                .findAny()
                .orElseGet(()-> null);
    }

    public ResponseLog findByIdAndDate(String id, Instant date){
        Key key = Key.builder()
                .partitionValue(id)
                .sortValue(String.valueOf(date))
                .build();

        return responseLogDynamoDbTable.getItem(key);
    }

    public ResponseLog update(ResponseLog responseLog){
        return responseLogDynamoDbTable.updateItem(responseLog);
    }

    public void delete(ResponseLog responseLog){
        responseLogDynamoDbTable.deleteItem(responseLog);
    }
}
