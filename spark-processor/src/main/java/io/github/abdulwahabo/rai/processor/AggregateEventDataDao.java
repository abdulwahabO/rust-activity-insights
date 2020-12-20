package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.exception.DaoException;
import io.github.abdulwahabo.rai.processor.model.AggregateEventData;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class AggregateEventDataDao {

    private final String table;

    public AggregateEventDataDao(String table) {
        this.table = table;
    }

    public void save(AggregateEventData data) throws DaoException {

        try {
            UpdateItemEnhancedRequest<AggregateEventData> request =
                    UpdateItemEnhancedRequest.builder(AggregateEventData.class).item(data).build();

            DynamoDbTable<AggregateEventData> table = dbTable();
            table.updateItem(request);
        } catch (DynamoDbException e) {
            throw new DaoException("Failed to write data to DynamoDB table", e);
        }
    }

    private DynamoDbTable<AggregateEventData> dbTable() {

        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                                                      .region(Region.US_EAST_1)
                                                      .build();

        DynamoDbEnhancedClient dbEnhancedClient = DynamoDbEnhancedClient.builder()
                                                                        .dynamoDbClient(dynamoDbClient)
                                                                        .build();

        return dbEnhancedClient.table(table, TableSchema.fromBean(AggregateEventData.class));
    }
}
