package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.exception.DaoException;
import org.apache.spark.sql.SparkSession;

public class SparkProcessorDriver {

    private static final String S3_FILE_PATH = "s3://"; // Todo
    private static final String DYNAMODB_TABLE = "s3//"; // todo

    public static void main(String[] args) {

        SparkSession spark = SparkSession.builder().appName("RustLang Github Activity Insights").getOrCreate();
        AggregateEventDataDao dataDao = new AggregateEventDataDaoImpl(DYNAMODB_TABLE);
        SparkProcessor processor = new SparkProcessor(dataDao, S3_FILE_PATH, spark);
        try {
            processor.start();
        } catch (DaoException e) {
            e.printStackTrace();
        } finally {
            spark.stop();
        }
    }
}
