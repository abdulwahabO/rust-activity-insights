package io.github.abdulwahabo.rai.dashboard.dao;

import io.github.abdulwahabo.rai.dashboard.model.AggregateEventData;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Service
public class AggregateEventDataDaoImpl implements AggregateEventDataDao {

    @Value("dynamodb.table") // TODO: put value in properties file.
    private String dynamoDbTAble;

    @Override
    public Optional<List<AggregateEventData>> get(String startDate, String endDate) {




        return Optional.empty();
    }

    private DynamoDbTable<AggregateEventData> dbTable() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                                                      .region(Region.US_EAST_1)
                                                      .build();

        DynamoDbEnhancedClient dbEnhancedClient = DynamoDbEnhancedClient.builder()
                                                                        .dynamoDbClient(dynamoDbClient)
                                                                        .build();

        return dbEnhancedClient.table(dynamoDbTAble, TableSchema.fromBean(AggregateEventData.class));
    }
}
