package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.exception.DaoException;
import io.github.abdulwahabo.rai.processor.model.AggregateEventData;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class AggregateEventDataDaoImpl implements AggregateEventDataDao {

    private Logger logger = LoggerFactory.getLogger(AggregateEventDataDaoImpl.class);
    private final String table;

    public AggregateEventDataDaoImpl(String table) {
        this.table = table;
    }

    @Override
    public void save(List<AggregateEventData> data) throws DaoException {
        for (AggregateEventData datum : data) {
            save(datum);
        }
    }

    private void save(AggregateEventData data) throws DaoException {
        try {
            UpdateItemEnhancedRequest<AggregateEventData> request =
                    UpdateItemEnhancedRequest.builder(AggregateEventData.class).item(data).build();
            DynamoDbTable<AggregateEventData> table = dbTable();
            table.updateItem(request);
        } catch (DynamoDbException e) {
            throw new DaoException("Failed to write data to DynamoDB table", e);
        }
        logger.info("Updated key: " + data.getDate() + " with data from " + data.getRepositoryData().size() + " repos");
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
