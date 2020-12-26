package io.github.abdulwahabo.rai.extractor;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExtractorUtilTest {

    private Path path;
    private String data = "some_value_121";

    @BeforeEach
    public void setup() throws Exception {
        path = Files.createTempFile("sample", ".txt");
    }

    @Test
    public void shouldReadAndWriteFile() throws Exception {
        ExtractorUtil extractorUtil = new ExtractorUtil();
        extractorUtil.saveToFile(data, path);
        String value = extractorUtil.readFile(path).get();
        Assertions.assertEquals(data, value);
    }
}
