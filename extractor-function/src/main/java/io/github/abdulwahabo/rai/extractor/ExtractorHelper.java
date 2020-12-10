package io.github.abdulwahabo.rai.extractor;

import io.github.abdulwahabo.rai.extractor.github.GithubEventDto;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class ExtractorHelper {

    // TODO: create this bucket.
    private static final String S3_BUCKET_NAME = "github-rustlang-events";
    private static final String S3_OBJECT_KEY = "";

    static final Function<GithubEventDto, S3EventDto> mapper = (githubEventDto -> {
        S3EventDto s3EventDto = new S3EventDto();
        s3EventDto.setRepository(githubEventDto.getRepo().getName());
        LocalDateTime dateTime = LocalDateTime.parse(githubEventDto.getTime());
        s3EventDto.setDate(dateTime.toLocalDate().toString());
        s3EventDto.setType(githubEventDto.getType());
        return s3EventDto;
    });
    static SdkHttpResponse writeToS3(File file) {
        S3Client s3Client = S3Client.builder().region(Region.US_EAST_1).build();
        PutObjectRequest request = PutObjectRequest.builder()
                                                   .key(S3_OBJECT_KEY)
                                                   .bucket(S3_BUCKET_NAME)
                                                   .build();

        PutObjectResponse response = s3Client.putObject(request, RequestBody.fromFile(file));
        return response.sdkHttpResponse();
    }

    static void saveEtag(String etag) {

        // todo: put it in a file in '/tmp'

        // Strip out qoutes.
        etag = etag.replace("\"", "");

    }

    static Optional<String> getETag() {

        return Optional.empty();
    }
}
