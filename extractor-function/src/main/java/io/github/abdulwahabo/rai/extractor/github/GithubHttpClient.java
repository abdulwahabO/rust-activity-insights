package io.github.abdulwahabo.rai.extractor.github;

import io.github.abdulwahabo.rai.extractor.exception.GithubClientException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

public class GithubHttpClient {

    /**
     * Returns an empty {@link Optional} if API returns a 304 response code. Otherwise the data from the response body
     * is contained in an {@link EventsResponse}.
     *
     * @param eTag The value of ETag header returned by the last successful request.
     * @param url The Github API endpoint to call.
     * @throws GithubClientException if a 4XX response or if the network request fails for any reason.
     */
    public Optional<EventsResponse> pollEvents(String eTag, String url) throws GithubClientException {

        // 1. Get raw JSON or throw Exception or return empty OPT if response is 308.
        // 2. Parse JSON and return list.
        // 3.

        // Get Etag header from response. and set it.

        return null;
    }

    private GithubEventDto parseJson(String eventJson) {

        return new GithubEventDto();
    }

    private HttpResponse<String> doRequest(String url, String etag)
            throws IOException, URISyntaxException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                                         .header("Accept", "application/vnd.github.v3+json")
                                         .header("User-Agent", "abdulwahabo")
                                         .header("If-None-Match", "\"" + etag + "\"")
                                         .GET()
                                         .build();

        HttpClient client = HttpClient.newBuilder()
                                      .version(HttpClient.Version.HTTP_1_1)
                                      .followRedirects(HttpClient.Redirect.NEVER)
                                      .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static class EventsResponse {
        private List<GithubEventDto> events;
        private String eTag;

        public List<GithubEventDto> getEvents() {
            return events;
        }

        public void setEvents(List<GithubEventDto> events) {
            this.events = events;
        }

        public String getETag() {
            return eTag;
        }

        public void setETag(String eTag) {
            this.eTag = eTag;
        }
    }
}
