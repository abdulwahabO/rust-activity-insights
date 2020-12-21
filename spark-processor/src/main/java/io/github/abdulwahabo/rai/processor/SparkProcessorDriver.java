package io.github.abdulwahabo.rai.processor;

public class SparkProcessorDriver {

    // todo
    private static final String S3_INPUT_FILE_PATH = "s3://";
    private static final String DYNAMODB_TABLE = "s3";

    public static void main(String[] args) {
        SparkProcessor processor = new SparkProcessor();
        processor.start(S3_INPUT_FILE_PATH, DYNAMODB_TABLE);
    }

    // todo: Create and stop SparkSession here.

}
