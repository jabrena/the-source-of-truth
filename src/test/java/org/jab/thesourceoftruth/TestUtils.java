package org.jab.thesourceoftruth;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestUtils {

    public static List<String> getContentAsList(final String path) {

        try {
            return Files.readAllLines(Paths.get(ClassLoader.getSystemResource(path).toURI()));
        }catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
