package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.exception.DynamoDbExportException;
import io.github.abdulwahabo.rai.processor.model.AggregateEventData;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

// TODO: Move this class into Loader-function.
public class AggregateEventDataDao {

    // todo: Keep it simple... A method to send out the Data model out to the DB.

    private final String table;

    public AggregateEventDataDao(String table) {
        this.table = table;
    }

    public void save(AggregateEventData data) throws DynamoDbExportException {

        DynamoDbTable<AggregateEventData> table = dbTable();


        // TODO: the Sdk supports the kind of Update i need to do..
        //       NO need to manually cycle through date....
        //       Read through docs extensively.

        // TODO: write a loader-function that does two simple things..
        //      1. Read the S3 and parse the JSON into objects.
        //      2. Send the objects to DynamoDB... use update features provided by SDK..



        try {

        } catch (DynamoDbException e) {

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
