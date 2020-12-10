package io.github.abdulwahabo.rai.extractor.exception;

public class GithubClientException extends Exception {

    public GithubClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public GithubClientException(String message) {
        super(message);
    }
}
