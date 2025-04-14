package mp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tag.jboss.modules.basic.spi.AService;
import tag.jboss.modules.basic.spi.BService;
import tag.jboss.modules.composite.api.CService;

import java.util.List;
import java.util.ServiceLoader;

/**
 * Test the service loader when using the modulepath.
 */
public class UseBasicFromMPTest {
    /**
     * Uses local META-INF/services/tag.jboss.modules.basic.spi.AService ->
     *  tag.jboss.modules.basic.provider.ProviderOfAService
     *  to be able to access the service.
     */
    @Test
    public void testUseAServiceFromMP() {
        ServiceLoader<AService> loader = ServiceLoader.load(AService.class);
        List<ServiceLoader.Provider<AService>> services = loader.stream().toList();
        services.forEach(p -> System.out.println(p.get()));
        Assertions.assertEquals(2, services.size(), "Should have found one service");
    }

    /**
     * This should pass
     */
    @Test
    public void testUseBServiceFromMP() {
        ServiceLoader<BService> loader = ServiceLoader.load(BService.class);
        List<ServiceLoader.Provider<BService>> services = loader.stream().toList();
        Assertions.assertEquals(1, services.size(), "Should be able to find the service");
    }
    @Test
    public void testUseBServiceFromMPViaClassLoader() {
        ServiceLoader<BService> loader = ServiceLoader.load(BService.class, BService.class.getClassLoader());
        List<ServiceLoader.Provider<BService>> services = loader.stream().toList();
        Assertions.assertEquals(1, services.size(), "Should be able to find the service");
    }
    @Test
    public void testUseCServiceFromMP() {
        ServiceLoader<CService> loader = ServiceLoader.load(CService.class);
        List<ServiceLoader.Provider<CService>> services = loader.stream().toList();
        Assertions.assertEquals(1, services.size(), "Should be able to find the service");
        CService service = services.getFirst().get();
        service.doCompositeWork();
    }
    @Test
    public void testUseCServiceFromMPViaClassLoader() {
        ClassLoader classLoader = CService.class.getClassLoader();
        System.out.println(classLoader);
        System.out.println(CService.class.getModule());
        System.out.println(CService.class.getModule().getLayer());
        ServiceLoader<CService> loader = ServiceLoader.load(CService.class, classLoader);
        List<ServiceLoader.Provider<CService>> services = loader.stream().toList();
        Assertions.assertEquals(1, services.size(), "Should be able to find the service");
        CService service = services.getFirst().get();
        service.doCompositeWork();
    }

    /**
     * Loader services from the module layer.
     */
    @Test
    public void testUseBServiceModuleLayer() {
        ModuleLayer layer = ModuleLayer.boot();
        System.out.println(layer);
        Module thisModule = UseBasicFromMPTest.class.getModule();
        System.out.println(thisModule);
        ModuleLayer thisLayer = thisModule.getLayer();
        System.out.println(thisLayer);

        ServiceLoader<BService> loader = ServiceLoader.load(thisLayer, BService.class);
        List<ServiceLoader.Provider<BService>> services = loader.stream().toList();
        Assertions.assertEquals(1, services.size(), "Should be able to find the service from layer");
    }
}
