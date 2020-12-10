package io.github.abdulwahabo.rai.extractor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;

import io.github.abdulwahabo.rai.extractor.exception.ExtractorException;
import io.github.abdulwahabo.rai.extractor.exception.GithubClientException;
import io.github.abdulwahabo.rai.extractor.github.GithubEventDto;
import io.github.abdulwahabo.rai.extractor.github.GithubHttpClient;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import software.amazon.awssdk.http.SdkHttpResponse;

public class Extractor implements RequestHandler<CloudWatchLogsEvent, Void> {

    private static final String EVENTS_API_URL = "https://api.github.com/orgs/rust-lang/events";

    // TODO: goto lambda config and increase the running time..

    @Override
    public Void handleRequest(CloudWatchLogsEvent event, Context context) {

        GithubHttpClient.EventsResponse eventsResponse;
        String eTag = ExtractorHelper.getETag().orElse("");
        LambdaLogger logger = context.getLogger();

        try {
            GithubHttpClient httpClient = new GithubHttpClient();
            Optional<GithubHttpClient.EventsResponse> eventsOptional = httpClient.pollEvents(eTag, EVENTS_API_URL);

            if (eventsOptional.isEmpty()) {
                logger.log("No new events to process");
                return null;
            }
            eventsResponse = eventsOptional.get();
        } catch (GithubClientException e) {
            throw new ExtractorException(e);
        }

        logger.log("Polled " + eventsResponse.getEvents().size() + " new events from Github");
        ExtractorHelper.saveEtag(eventsResponse.getETag());

        List<GithubEventDto> githubDtos = eventsResponse.getEvents();
        List<S3EventDto> s3Dtos = githubDtos.stream().map(ExtractorHelper.mapper).collect(Collectors.toList());

        SdkHttpResponse sdkHttpResponse = exportToS3(s3Dtos);
        String status = sdkHttpResponse.statusText().orElse("<No status text was returned>");
        logger.log("S3 response code: " + sdkHttpResponse.statusCode());
        logger.log("S3 status text: " + status);

        logger.log("Finished run with " + context.getRemainingTimeInMillis() + " milliseconds left");
        return null;
    }

    private SdkHttpResponse exportToS3(List<S3EventDto> eventDtos) {
        try {

            Path path = Files.createTempFile(Paths.get("/tmp"),"s3-data", ".json");
            File tempFile = new File(path.toUri());

            ObjectWriter objectWriter = new ObjectMapper().writerFor(S3EventDto.class).withRootValueSeparator("\n");
            SequenceWriter sequenceWriter = objectWriter.writeValues(tempFile);
            sequenceWriter.writeAll(eventDtos);
            sequenceWriter.close();

            SdkHttpResponse sdkHttpResponse = ExtractorHelper.writeToS3(tempFile);
            Files.delete(tempFile.toPath());
            return sdkHttpResponse;

        } catch (Exception e) {
            throw new ExtractorException(e);
        }
    }
}
