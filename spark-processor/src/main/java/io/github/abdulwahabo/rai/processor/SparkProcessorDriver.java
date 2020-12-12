package io.github.abdulwahabo.rai.processor;

public class SparkProcessorDriver {

    private static final String S3_INPUT_FILE_PATH = "s3://";
    private static final String S3_OUTPUT_FILE_PATH = "s3://";

    public static void main(String[] args) {
        SparkProcessor processor = new SparkProcessor(S3_INPUT_FILE_PATH, S3_OUTPUT_FILE_PATH);
        processor.start();
    }

    // TODO: Write to an S3 file, for which a lambda function would be triggered:
    //        Reason 1: New data for a date that already has a DB entry might become available.
    //                  So the data would have to be merged into the old one. can't be done here.
    //        Reason 2: The write to DynamoDB might fail. That way data is lost.

    // TODO: Potential Problems:
    //       Check that the JSON field names written to s3 are compatible with field names of the Spark Program's
    //       model class.
}
