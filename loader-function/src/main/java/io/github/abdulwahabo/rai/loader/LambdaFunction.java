package io.github.abdulwahabo.rai.loader;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

public class LambdaFunction implements RequestHandler<S3Event, Void> {

    @Override
    public Void handleRequest(S3Event input, Context context) {

        // TODO: get the object key and bucket name from this record below... Get region too.
        //       input.getRecords().get(0).getS3().getBucket()

        // TODO: use details from above to construct a loader??


        return null;
    }

    // TODO: Make a common module: put two classes: S3eventDto and the data model used for dynamoDB\
    //          Also move common depeendencies into this module.
}
