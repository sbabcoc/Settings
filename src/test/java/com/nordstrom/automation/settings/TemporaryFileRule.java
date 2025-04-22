package com.nordstrom.automation.settings;

import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.nio.file.*;

public class TemporaryFileRule extends ExternalResource {

    private Path tempFile;

    @Override
    protected void before() throws IOException {
        Path homeDir = Paths.get(System.getProperty("user.home"));
        tempFile = Files.createTempFile(homeDir, "test-", ".properties");
    }

    @Override
    protected void after() {
        if (tempFile != null && Files.exists(tempFile)) {
            try {
                Files.delete(tempFile);
            } catch (IOException e) {
                System.err.println("Failed to delete temp file: " + tempFile);
            }
        }
    }

    public Path getTempFile() {
        return tempFile;
    }
}
