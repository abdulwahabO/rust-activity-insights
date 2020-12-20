package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.model.AggregateEventData;
import io.github.abdulwahabo.rai.processor.model.EventData;

import java.util.List;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.KeyValueGroupedDataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.TypedColumn;
import scala.Tuple2;

public class SparkProcessor {

    void start(String file, String dynamoTable) {

        SparkSession spark = SparkSession.builder().appName("RustLang Activity Insights").getOrCreate();

        Encoder<EventData> encoder = Encoders.bean(EventData.class);
        Dataset<EventData> dataset = spark.read().json(file).as(encoder);
        dataset.cache();

        KeyValueGroupedDataset<String, EventData> groupedDataset =
                dataset.groupByKey(eventKeyMapper, Encoders.STRING());

        // TODO: Aggregate the dataSet using Aggregator interface.

        EventDataAggregator aggregator = new EventDataAggregator();
        TypedColumn<EventData, List<AggregateEventData.RepositoryData>> column = aggregator.toColumn();

        Encoder<AggregateEventData> aggregateEncoder = Encoders.bean(AggregateEventData.class);

        // TODO: Don't collect as list... write to S3 as JSON.
        List<AggregateEventData> finalData = groupedDataset.agg(column)
                                                           .map(aggregateEventMapper, aggregateEncoder)
                                                           .collectAsList();

        //
        AggregateEventDataDao dataDao = new AggregateEventDataDao(dynamoTable);

        dataDao.save(finalData.get(0)); // TODO: catch and stop() the spark session
        spark.stop();
    }

    // Needs to be declared explicitly to prevent ambiguity in call to Dataset#groupByKey()
    private MapFunction<EventData, String> eventKeyMapper = EventData::getDate;

    private MapFunction<Tuple2<String, List<AggregateEventData.RepositoryData>>, AggregateEventData>
            aggregateEventMapper = (tuple) -> {


        return new AggregateEventData();
    };
}
