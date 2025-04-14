package cp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tag.jboss.modules.basic.spi.AService;
import tag.jboss.modules.basic.spi.BService;

import java.util.List;
import java.util.ServiceLoader;

/**
 * Test the service loader when using the classpath.
 */
public class UseBasicFromCPTest {
    /**
     * Uses local META-INF/services/tag.jboss.modules.basic.spi.AService ->
     *  tag.jboss.modules.basic.provider.ProviderOfAService
     *  to be able to access the service.
     */
    @Test
    public void testUseAServiceFromCP() {
        ServiceLoader<AService> loader = ServiceLoader.load(AService.class);
        List<ServiceLoader.Provider<AService>> services = loader.stream().toList();
        Assertions.assertEquals(1, services.size(), "Should have found one service");
    }

    /**
     * This will fail to find the service because there is no META-INF/services/tag.jboss.modules.basic.spi.BService
     */
    @Test
    public void testUseBServiceFromCP() {
        ServiceLoader<BService> loader = ServiceLoader.load(BService.class);
        List<ServiceLoader.Provider<BService>> services = loader.stream().toList();
        Assertions.assertEquals(0, services.size(), "Should be unable to find the service");
    }

    /**
     * See if one can use the service loader with the module layer for the unnamed module.
     * This will fail because the unnamed module has no module layer.
     */
    @Test
    public void testUseBService() {
        ModuleLayer layer = ModuleLayer.boot();
        System.out.println(layer);
        Module thisModule = UseBasicFromCPTest.class.getModule();
        System.out.println(thisModule);
        ModuleLayer thisLayer = thisModule.getLayer();
        System.out.println(thisLayer);

        NullPointerException npe = Assertions.assertThrows(NullPointerException.class,
                () -> ServiceLoader.load(thisLayer, BService.class));
        Assertions.assertNotNull(npe, "Expected a NullPointerException from thisLayer");
    }
}
