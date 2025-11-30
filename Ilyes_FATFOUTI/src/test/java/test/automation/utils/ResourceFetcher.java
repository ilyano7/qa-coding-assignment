package test.automation.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class ResourceFetcher {

    public static File getResourceAsFile(String path) throws URISyntaxException {
        return Paths.get(ResourceFetcher.class.getClassLoader().getResource(path).toURI()).toFile();
    }

    public static InputStream getResourceAsStream(String path) {
        return ResourceFetcher.class.getClassLoader().getResourceAsStream(path);
    }

}
