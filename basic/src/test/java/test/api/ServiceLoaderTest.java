package test.api;

import org.junit.jupiter.api.Test;
import tag.jboss.auto.spi.LeakyLegacyService;
import tag.jboss.modules.basic.spi.AService;
import tag.jboss.modules.basic.spi.BService;
import tag.jboss.modules.open.spi.SerializationService;

import java.sql.SQLException;
import java.util.Properties;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServiceLoaderTest {
    @Test
    public void testUndelcaredBServiceUsage() {
        System.out.println("ServiceLoaderTest.testUndelcaredBServiceUsage");
        Module module = ServiceLoaderTest.class.getModule();
        if(module.isNamed()) {
            ServiceConfigurationError error = assertThrows(ServiceConfigurationError.class, () -> {
                System.out.println("ServiceLoaderTest.testUndelcaredBServiceUsage: " + ServiceLoader.load(BService.class));
            });
            System.out.println("Saw error: " + error);
        }
    }
    @Test
    public void testDeclaredAServiceUsage() {
        System.out.println("ServiceLoaderTest.testDeclaredAServiceUsage");
        ServiceLoader<AService> loader = ServiceLoader.load(AService.class);
        for (AService service : loader) {
            System.out.println("ServiceLoaderTest.testDeclaredAServiceUsage: " + service);
        }
    }
    @Test
    public void testLeakyLegacyServiceUsage() {
        System.out.println("ServiceLoaderTest.testLeakyLegacyServiceUsage");
        ServiceLoader.load(LeakyLegacyService.class).forEach(service -> {
            System.out.println("ServiceLoaderTest.testLeakyLegacyServiceUsage: " + service);
            try {
                Properties props = service.loadConfig();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testSerializationService() {
        System.out.println("ServiceLoaderTest.testSerializationService");
        ServiceLoader.load(SerializationService.class).forEach(service -> {
            System.out.println("ServiceLoaderTest.testSerializationService: " + service);
            service.serialize("Does nothing");
        });
    }
}
