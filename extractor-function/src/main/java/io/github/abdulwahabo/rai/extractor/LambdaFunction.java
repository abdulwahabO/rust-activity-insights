package io.github.abdulwahabo.rai.extractor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent;

import io.github.abdulwahabo.rai.extractor.github.GithubHttpClient;
import io.github.abdulwahabo.rai.extractor.s3.S3BucketClient;

// This is the entry point used by the AWS Lambda Function.
public class LambdaFunction implements RequestHandler<CloudWatchLogsEvent, Void> {

    // TODO: goto lambda config and increase the running time..

    private final Extractor extractor =
            new Extractor(new S3BucketClient(), new GithubHttpClient(), new ExtractorUtil());

    @Override
    public Void handleRequest(CloudWatchLogsEvent input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("AWS Request ID:" + context.getAwsRequestId());
        extractor.extract();
        logger.log("Finished run with " + context.getRemainingTimeInMillis() + " milliseconds left");
        return null;
    }
}
