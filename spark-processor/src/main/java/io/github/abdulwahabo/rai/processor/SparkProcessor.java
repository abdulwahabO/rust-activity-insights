package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.model.EventDataModel;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.KeyValueGroupedDataset;
import org.apache.spark.sql.SparkSession;

public class SparkProcessor {

    private String inputFile;
    private String outputFile;

    SparkProcessor(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    void start() {

        SparkSession spark = SparkSession.builder().appName("RustLang Activity Insights").getOrCreate();

        Encoder<EventDataModel> encoder = Encoders.bean(EventDataModel.class);
        Dataset<EventDataModel> dataset = spark.read().json(inputFile).as(encoder);
        dataset.cache();

        // TODO: Open online docs for this class and study it's methods.
        KeyValueGroupedDataset<String, EventDataModel> groupedDataset;

        // TODO: Using UDFs to extract data

        // TODO: need to use a aggregator/transformation that takes an Encoder to create a new DataSet.

        // Writing is a an actio
        dataset.write().json("");

        // TODO: First things first, Group by date...
        //          For each group, group by repository again

        // TODO:::::: create new project module... loader-function

        // TODO: One way to avoid a Loader function:
        //        Look into stateful transformations so that the Spark processor remembers the values ?????

    }
}
