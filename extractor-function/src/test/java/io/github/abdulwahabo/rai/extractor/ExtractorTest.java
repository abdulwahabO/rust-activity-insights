package io.github.abdulwahabo.rai.extractor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.abdulwahabo.rai.extractor.github.GithubEventDto;
import io.github.abdulwahabo.rai.extractor.github.GithubHttpClient;
import io.github.abdulwahabo.rai.extractor.s3.S3BucketClient;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExtractorTest {

    private S3BucketClient s3BucketClient = mock(S3BucketClient.class);
    private GithubHttpClient githubHttpClient = mock(GithubHttpClient.class);
    private ExtractorUtil extractorUtil = mock(ExtractorUtil.class);

    private final String etag = "some-value";

    @BeforeEach
    public void setup() throws Exception {
        when(githubHttpClient.pollEvents(anyString(), anyString())).thenReturn(Optional.of(eventDtos()));
        when(extractorUtil.readFile(any(Path.class))).thenReturn(Optional.of(etag));
    }

    @Test
    public void shouldWriteToS3() throws Exception {
        Extractor extractor = new Extractor(s3BucketClient, githubHttpClient, extractorUtil);
        extractor.extract();
        verify(githubHttpClient).pollEvents(etag, "https://api.github.com/orgs/rust-lang/events");
        verify(extractorUtil).readFile(any(Path.class));
        verify(extractorUtil).saveToFile(etag, Paths.get("/tmp", "etag.txt"));
        verify(s3BucketClient).writeFile(any(File.class), anyString(), anyString());
    }

    private GithubHttpClient.EventsResponse eventDtos() {
        GithubEventDto.Repository repository = new GithubEventDto.Repository();
        repository.setId("000");
        repository.setName("repo_1");

        GithubEventDto.User user = new GithubEventDto.User();
        user.setId("000");
        user.setUsername("user");

        GithubEventDto eventDto = new GithubEventDto();
        eventDto.setType("type");
        eventDto.setTime("2020-02-02T09:13:00");
        eventDto.setActor(user);
        eventDto.setRepo(repository);
        eventDto.setId("Id");

        GithubHttpClient.EventsResponse response = new GithubHttpClient.EventsResponse();
        response.setEvents(Collections.singletonList(eventDto));
        response.setETag(etag);
        return response;
    }
}
