package io.github.abdulwahabo.rai.extractor.exception;

public class S3ClientException extends Exception {
    public S3ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public S3ClientException(String message) {
        super(message);
    }
}
