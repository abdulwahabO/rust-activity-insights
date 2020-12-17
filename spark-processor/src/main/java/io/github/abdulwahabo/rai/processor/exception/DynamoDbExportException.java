package io.github.abdulwahabo.rai.processor.exception;

public class DynamoDbExportException extends Exception {

    public DynamoDbExportException(String message) {
        super(message);
    }

    public DynamoDbExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
