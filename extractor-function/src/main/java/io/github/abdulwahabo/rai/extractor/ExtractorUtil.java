package io.github.abdulwahabo.rai.extractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class ExtractorUtil {

    /**
     * Save the given string to the file represented by the given {@link Path}.
     */
    public void saveToFile(String value, Path path) throws IOException {
        Files.writeString(path, value);
    }

    /**
     * Returns the file represented by the given {@link Path};
     */
    public Optional<String> readFile(Path path) throws IOException {
        String etag = Files.readString(path);
        return etag.isBlank() ? Optional.empty() : Optional.of(etag);
    }
}
