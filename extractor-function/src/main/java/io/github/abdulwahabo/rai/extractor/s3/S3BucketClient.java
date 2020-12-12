package io.github.abdulwahabo.rai.extractor.s3;

import io.github.abdulwahabo.rai.extractor.exception.S3ClientException;

import java.io.File;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class S3BucketClient {

    public void writeFile(File file, String key, String bucket) throws S3ClientException {
        S3Client s3Client = S3Client.builder().region(Region.US_EAST_1).build();
        PutObjectRequest request = PutObjectRequest.builder()
                                                   .key(key)
                                                   .bucket(bucket)
                                                   .build();

        try {
            PutObjectResponse response = s3Client.putObject(request, RequestBody.fromFile(file));
            SdkHttpResponse httpResponse = response.sdkHttpResponse();
            if (!httpResponse.isSuccessful()){
                throw new S3ClientException("Failed to write file. Returned code: " + httpResponse.statusCode());
            }
        } catch (S3Exception e) {
            throw new S3ClientException("Failed to write file", e);
        }
    }

    // TODO: Add a read method and move to common module.
}
