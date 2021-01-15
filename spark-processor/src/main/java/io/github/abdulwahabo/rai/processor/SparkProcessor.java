package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.exception.DaoException;
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

    private AggregateEventDataDao dataDao;
    private String file;
    private SparkSession sparkSession;

    public SparkProcessor(AggregateEventDataDao dataDao, String file, SparkSession sparkSession) {
        this.dataDao = dataDao;
        this.file = file;
        this.sparkSession = sparkSession;
    }

    void start() throws DaoException {
        Encoder<EventData> encoder = Encoders.bean(EventData.class);
        Dataset<EventData> dataset = sparkSession.read().json(file).as(encoder);
        dataset.cache();
        KeyValueGroupedDataset<String, EventData> groupedDataset = dataset.groupByKey(eventKeyMapper, Encoders.STRING());

        EventDataAggregator aggregator = new EventDataAggregator();
        TypedColumn<EventData, AggregateEventData.RepositoryDataWrapper> column = aggregator.toColumn();
        Dataset<Tuple2<String, AggregateEventData.RepositoryDataWrapper>> tuple2Dataset = groupedDataset.agg(column);

        Encoder<AggregateEventData> aggregateEncoder = Encoders.bean(AggregateEventData.class);
        Dataset<AggregateEventData> aggregateEventDataDataset = tuple2Dataset.map(aggregateMapper, aggregateEncoder);
        List<AggregateEventData> finalData = aggregateEventDataDataset.collectAsList();
        dataDao.save(finalData);
    }

    // Declared explicitly to prevent ambiguity in call to Dataset#groupByKey()
    private MapFunction<EventData, String> eventKeyMapper = EventData::getDate;

    private MapFunction<Tuple2<String, AggregateEventData.RepositoryDataWrapper>, AggregateEventData>
            aggregateMapper = (tuple) -> {
        AggregateEventData result = new AggregateEventData();
        result.setDate(tuple._1);
        List<AggregateEventData.RepositoryData> repositoryData = tuple._2.getRepositoryDataList();
        result.setRepositoryData(repositoryData);
        return result;
    };
}
