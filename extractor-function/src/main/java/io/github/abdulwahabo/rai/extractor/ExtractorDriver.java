package io.github.abdulwahabo.rai.extractor;

import io.github.abdulwahabo.rai.extractor.github.GithubHttpClient;
import io.github.abdulwahabo.rai.extractor.s3.S3BucketClient;

// Main class for starting the extractor outside an AWS Lambda environment.
public class ExtractorDriver {
    public static void main(String[] args) {
        Extractor extractor = new Extractor(new S3BucketClient(), new GithubHttpClient(), new ExtractorUtil());
        extractor.extract();
    }
}
