package io.github.abdulwahabo.rai.dashboard.dao;

import io.github.abdulwahabo.rai.dashboard.model.AggregateEventData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Service
public class AggregateEventDataDaoImpl implements AggregateEventDataDao {

    @Override
    public Optional<List<AggregateEventData>> get(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
        List<AggregateEventData> dataList = dbTable().scan().items().stream().filter(data -> {
           LocalDate date = LocalDate.parse(data.getDate());
           boolean result = false;
           if (date.isEqual(start) || date.isEqual(end) || (date.isAfter(start) && date.isBefore(end))) {
               result = true;
           }
           return result;
        }).collect(Collectors.toList());
        return Optional.of(dataList);
    }

    private DynamoDbTable<AggregateEventData> dbTable() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                                                      .region(Region.US_EAST_1)
                                                      .build();

        DynamoDbEnhancedClient dbEnhancedClient = DynamoDbEnhancedClient.builder()
                                                                        .dynamoDbClient(dynamoDbClient)
                                                                        .build();

        return dbEnhancedClient.table("rust-event-data", TableSchema.fromBean(AggregateEventData.class));
    }
}
