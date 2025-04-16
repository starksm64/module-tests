package test.resource;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JrtUrlTest {
    @Test
    public void testRootFileURL() throws IOException {
        // The root file URL
        String configPropsRef = "jrt:/tag.jboss.basic/config.properties";
        URL url = new URL(configPropsRef);
        assertThrows(IOException.class, () -> {
                    System.out.println("URL: " + url.getContent());
                }
        );
    }
}
