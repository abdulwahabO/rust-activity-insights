package io.github.abdulwahabo.rai.dashboard.dao;

import io.github.abdulwahabo.rai.dashboard.model.AggregateEventData;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Service
public class AggregateEventDataDaoImpl implements AggregateEventDataDao {

    @Value("dynamodb.table") // TODO: put value in properties file.
    private String dynamoDbTAble;

    @Override
    public Optional<List<AggregateEventData>> get(String startDate, String endDate) {
        Key start = Key.builder().partitionValue(startDate).build();
        Key end = Key.builder().partitionValue(endDate).build();
        QueryConditional conditional = QueryConditional.sortBetween(start, end);
        List<AggregateEventData> data = dbTable().query(conditional).items().stream().collect(Collectors.toList());
        return Optional.of(data);
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
