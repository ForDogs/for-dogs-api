package com.fordogs.configuraion;

import com.fordogs.configuraion.properties.AWSProperties;
import com.fordogs.core.util.logging.log.RequestLog;
import com.fordogs.core.util.logging.log.ResponseLog;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class DynamoDBConfiguration {

    private final AWSProperties awsProperties;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        DynamoDbClientBuilder dynamoDbClientBuilder = DynamoDbClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(awsProperties.getCredentials().getAccessKey(), awsProperties.getCredentials().getSecretKey())
                        )
                );

        if (awsProperties.getDynamodb() != null && awsProperties.getDynamodb().getEndpoint() != null) {
            dynamoDbClientBuilder.endpointOverride(URI.create(awsProperties.getDynamodb().getEndpoint()));
        }

        return dynamoDbClientBuilder.build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        createTableIfNotExists(dynamoDbClient, enhancedClient, ResponseLog.class, 10L, 10L);
        createTableIfNotExists(dynamoDbClient, enhancedClient, RequestLog.class, 10L, 10L);

        return enhancedClient;
    }

    private <T> void createTableIfNotExists(DynamoDbClient dynamoDbClient, DynamoDbEnhancedClient enhancedClient, Class<T> tableClass, long readCapacityUnits, long writeCapacityUnits) {
        String tableName = tableClass.getSimpleName();
        try {
            DescribeTableRequest describeTableRequest = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();
            dynamoDbClient.describeTable(describeTableRequest);
        } catch (ResourceNotFoundException e) {
            TableSchema<T> tableSchema = TableSchema.fromBean(tableClass);

            CreateTableEnhancedRequest createTableRequest = CreateTableEnhancedRequest.builder()
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(readCapacityUnits)
                            .writeCapacityUnits(writeCapacityUnits)
                            .build())
                    .build();

            DynamoDbTable<T> table = enhancedClient.table(tableName, tableSchema);
            table.createTable(createTableRequest);
        }
    }
}
