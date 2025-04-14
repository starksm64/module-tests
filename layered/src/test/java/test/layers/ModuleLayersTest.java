package test.layers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tag.jboss.modules.basic.spi.AService;
import test.layers.jpms.TestFinder;

import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleLayersTest {
    /**
     * Test loading the tag.jboss.basic into a new ModuleLayer with its
     * own ClassLoader. The working directory of the test is assumed to
     * be the basedir of the layered subproject for the relative nio path
     * to the tag.jboss.basic module.
     */
    @Test
    public void testBasicModuleLayerAService() {
        Path modulePath = Path.of("../basic/target/classes");
        ModuleLayer basicLayer = createModuleLayer(modulePath);
        Optional<Module> basicModule = basicLayer.findModule("tag.jboss.basic");
        basicLayer.modules().iterator().forEachRemaining(System.out::println);
        Assertions.assertTrue(basicModule.isPresent(), "tag.jboss.basic module should be found");
        ClassLoader basicLoader = basicLayer.findLoader("tag.jboss.basic");
        System.out.println(basicLoader);
        Assertions.assertNotNull(basicLoader, "ClassLoader for tag.jboss.basic module should not be null");
        AService aServiceBL = ServiceLoader
                .load(AService.class, basicLoader)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("AService not found"));
        AService aServiceBM = ServiceLoader
                .load(basicLayer, AService.class)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("AService not found"));
        Assertions.assertSame(aServiceBL.getClass(), aServiceBM.getClass());
        System.out.println("AService impl: "+aServiceBL.getClass());
        System.out.println("AService.class module: "+aServiceBL.getClass().getModule());
    }

    @Test
    public void testOverrideModuleLayerAService() {
        Path basicPath = Path.of("../basic/target/classes");
        ModuleLayer basicLayer = createModuleLayer(basicPath);
        Path modulePath = Path.of("target/classes");
        ModuleLayer thisLayer = createModuleLayer(modulePath, basicLayer);

        Optional<Module> layeredModule = thisLayer.findModule("tag.jboss.layered");
        Assertions.assertTrue(layeredModule.isPresent(), "tag.jboss.layered module should be found");

        ServiceLoader<AService> loader = ServiceLoader.load(thisLayer, AService.class);
        loader.iterator().forEachRemaining(System.out::println);
    }
    @Test
    public void testOverrideModuleLayerAServiceCL() {
        Path basicPath = Path.of("../basic/target/classes");
        ModuleLayer basicLayer = createModuleLayer(basicPath);
        Path modulePath = Path.of("target/classes");
        ModuleLayer thisLayer = createModuleLayer(modulePath, basicLayer);

        Optional<Module> layeredModule = thisLayer.findModule("tag.jboss.layered");
        Assertions.assertTrue(layeredModule.isPresent(), "tag.jboss.layered module should be found");

        ClassLoader layeredLoader = thisLayer.findLoader("tag.jboss.layered");
        ServiceLoader<AService> loader = ServiceLoader.load(AService.class, layeredLoader);
        loader.iterator().forEachRemaining(System.out::println);
    }

    ModuleLayer createModuleLayer(Path modulePath) {
        return createModuleLayer(modulePath, ModuleLayer.boot());
    }
    ModuleLayer createModuleLayer(Path modulePath, ModuleLayer parent) {
        Assertions.assertTrue(modulePath.toFile().exists(), modulePath.toAbsolutePath()+" exists");
        ModuleFinder finder = ModuleFinder.of(modulePath);
        Set<String> roots = finder.findAll()
                .stream()
                .map(m -> m.descriptor().name())
                .collect(Collectors.toSet());

        Configuration theConfig = parent.configuration().resolve(finder, ModuleFinder.of(), roots);
        ModuleLayer.Controller theController = ModuleLayer
                .defineModulesWithOneLoader(theConfig, List.of(parent), ClassLoader.getSystemClassLoader());
        ModuleLayer theLayer = theController.layer();
        return theLayer;
    }

    /**
     * Validate the test.layers.jpms.TestFinder and TestModuleReader
     * @throws IOException
     */
    @Test
    public void testModuleFinder() throws IOException {
        Path modulePath = Path.of("target/classes");
        Assertions.assertTrue(modulePath.toFile().exists(), modulePath.toAbsolutePath()+" exists");
        ModuleFinder finder = ModuleFinder.of(modulePath);

        ModuleReference layered = finder.find("tag.jboss.layered").get();
        ModuleReader reader = layered.open();
        HashSet<String> jdkFinderNames = new HashSet<>();
        reader.list().forEach(jdkFinderNames::add);
        System.out.println(jdkFinderNames);

        // Try the custom ModuleFinder
        ModuleFinder testFinder = new TestFinder();
        ModuleReference layeredTest = testFinder.find("tag.jboss.layered").get();
        ModuleReader readerTest = layeredTest.open();
        HashSet<String> testFinderNames = new HashSet<>();
        readerTest.list().forEach(testFinderNames::add);
        System.out.println(testFinderNames);

        Assertions.assertEquals(jdkFinderNames, testFinderNames);

        System.out.println("JDK ModuleFinder.findAll():");
        finder.findAll().iterator().forEachRemaining(System.out::println);
        System.out.println("TestFinder.findAll():");
        testFinder.findAll().iterator().forEachRemaining(System.out::println);
    }
}
