package io.github.abdulwahabo.rai.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;

import io.github.abdulwahabo.rai.extractor.exception.ExtractorException;
import io.github.abdulwahabo.rai.extractor.exception.GithubClientException;
import io.github.abdulwahabo.rai.extractor.exception.S3ClientException;
import io.github.abdulwahabo.rai.extractor.github.GithubEventDto;
import io.github.abdulwahabo.rai.extractor.github.GithubHttpClient;
import io.github.abdulwahabo.rai.extractor.s3.S3BucketClient;
import io.github.abdulwahabo.rai.extractor.s3.S3EventDto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extractor {

    private static final String EVENTS_API_URL = "https://api.github.com/orgs/rust-lang/events";
    private static final Path ETAG_STORAGE_FILE = Paths.get("/tmp", "etag.txt");

    private static final String S3_BUCKET_NAME = "filebox-storage";
    private static final String S3_OBJECT_KEY = "rust_activities_data";
    private Logger logger = LoggerFactory.getLogger(Extractor.class);

    private GithubHttpClient githubClient;
    private S3BucketClient s3BucketClient;
    private ExtractorUtil helper;

    public Extractor(S3BucketClient s3client, GithubHttpClient githubClient, ExtractorUtil helper) {
        this.s3BucketClient = s3client;
        this.githubClient = githubClient;
        this.helper = helper;
    }

    private final Function<GithubEventDto, S3EventDto> eventDtoMapper = (githubEventDto -> {
        S3EventDto s3EventDto = new S3EventDto();
        s3EventDto.setRepository(githubEventDto.getRepo().getName());
        LocalDateTime dateTime = LocalDateTime.parse(githubEventDto.getTime(), DateTimeFormatter.ISO_DATE_TIME);
        s3EventDto.setDate(dateTime.toLocalDate().toString());
        s3EventDto.setType(githubEventDto.getType());
        return s3EventDto;
    });

    public void extract() throws ExtractorException {
        try {
            if (!Files.exists(ETAG_STORAGE_FILE)) {
                Files.createFile(ETAG_STORAGE_FILE);
            }

            String eTag = helper.readFile(ETAG_STORAGE_FILE).orElse("");
            Optional<GithubHttpClient.EventsResponse> eventsOptional = githubClient.pollEvents(eTag, EVENTS_API_URL);

            if(eventsOptional.isPresent()) {
                GithubHttpClient.EventsResponse  eventsResponse = eventsOptional.get();
                List<GithubEventDto> githubDtos = eventsResponse.getEvents();
                logger.info("Polled " + githubDtos.size() + " new events from Github");

                eTag = eventsResponse.getETag();
                eTag = eTag.replace("\"", "");  // Remove double quotes.
                helper.saveToFile(eTag, ETAG_STORAGE_FILE);

                List<S3EventDto> s3Dtos = githubDtos.stream().map(eventDtoMapper).collect(Collectors.toList());
                Path path = Files.createTempFile(Paths.get("/tmp"), "s3-data", ".json");
                File tempFile = new File(path.toUri());
                prepareFile(s3Dtos, tempFile);

                s3BucketClient.writeFile(tempFile, S3_OBJECT_KEY, S3_BUCKET_NAME);
            } else {
                logger.info("No new events from Github API");
            }
        } catch (IOException | GithubClientException | S3ClientException e) {
            throw new ExtractorException(e);
        }
    }

    private void prepareFile(List<S3EventDto> s3Dtos, File file) throws IOException {
        ObjectWriter objectWriter = new ObjectMapper().writerFor(S3EventDto.class).withRootValueSeparator("\n");
        SequenceWriter sequenceWriter = objectWriter.writeValues(file);
        sequenceWriter.writeAll(s3Dtos);
        sequenceWriter.close();
    }
}
