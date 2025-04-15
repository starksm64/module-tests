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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleLayersTest {
    @Test
    public void testFindLoadedBasicModule() {
        ModuleLayer layer = ModuleLayer.boot();
        Optional<Module> basicModule = layer.findModule("tag.jboss.basic");
        Assertions.assertTrue(basicModule.isPresent(), "tag.jboss.basic module should be found");
        System.out.println(basicModule.get());
    }

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
        AService aServiceBM = ServiceLoader
                .load(basicLayer, AService.class)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("AService not found"));
        AService aServiceBL = ServiceLoader
                .load(AService.class, basicLoader)
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

    /**
     */
    @Test
    public void testOverrideModuleLayerAServiceCL() {
        Path basicPath = Path.of("../basic/target/classes");
        ModuleLayer basicLayer = createModuleLayer(basicPath);
        ClassLoader basicLoader = basicLayer.findLoader("tag.jboss.basic");
        System.out.println(basicLoader);
        System.out.println(basicLoader.getParent());
        Path modulePath = Path.of("target/classes");
        ModuleLayer thisLayer = createModuleLayer(modulePath, basicLayer);

        Optional<Module> layeredModule = thisLayer.findModule("tag.jboss.layered");
        Assertions.assertTrue(layeredModule.isPresent(), "tag.jboss.layered module should be found");

        ClassLoader layeredLoader = thisLayer.findLoader("tag.jboss.layered");
        System.out.println(layeredLoader);
        System.out.println(layeredLoader.getParent());
        ServiceLoader<AService> loader = ServiceLoader.load(AService.class, layeredLoader);
        loader.iterator().forEachRemaining(System.out::println);
    }

    /**
     ModuleLayer L1
     ModuleLayer L2 which is a child layer of L1

     ClassLoader C1
     ClassLoader C2

     Module M1 in layer L1 defined to C1 providing service S1
     Module M2 in layer L2 defined to C2

     If you load service S1 directly from L1 or L2 using the `ServiceLoader.load(layer, service)` API, or from C1 using the usual API, then the service will be found.

     If you load service S1 from C2, the service will not be found, even though L2 is in C2 and L2 is a child of L1.
     */
    @Test
    public void testIsolatedLayersBServiceCL() throws Exception {
        // L1, C1, M1
        Path M1path = Path.of("../basic/target/classes");
        ModuleLayer boot = ModuleLayer.boot();
        ModuleLayer L1 = createIsolatedModuleLayer(M1path, boot);
        ClassLoader C1 = L1.findLoader("tag.jboss.basic");
        System.out.println(C1);
        System.out.println("C1.parent: "+C1.getParent());
        Optional<Module> M1opt = L1.findModule("tag.jboss.basic");
        Assertions.assertTrue(M1opt.isPresent(), "tag.jboss.basic module should be found");
        Module M1 = M1opt.get();
        System.out.printf("%s/%s\n", M1, M1.hashCode());

        // L2, C2, M2
        Path M2path = Path.of("target/classes");
        ModuleLayer L2 = createIsolatedModuleLayer(M2path, L1);
        Optional<Module> M2opt = L2.findModule("tag.jboss.layered");
        Assertions.assertTrue(M2opt.isPresent(), "tag.jboss.layered module should be found");
        Module M2 = M2opt.get();
        ClassLoader C2 = L2.findLoader("tag.jboss.layered");
        System.out.println(C2);
        System.out.println("C2.parent: "+C2.getParent());

        Class<?> S1Class = C1.loadClass("tag.jboss.modules.basic.spi.BService");
        // Load the service S1 from L2
        ServiceLoader<?> loaderL2 = ServiceLoader.load(L2, S1Class);
        loaderL2.iterator().forEachRemaining(System.out::println);
        Optional<?> firstL2 = loaderL2.findFirst();
        if(firstL2.isPresent()) {
            System.out.println("Found S1 from L2: "+firstL2.get());
            // Where is this from?
            Module module = firstL2.get().getClass().getModule();
            System.out.printf("S1.module: ", module);
            System.out.printf("%s/%s, layer: %s\n", module, module.hashCode(), module.getLayer());
        } else {
            System.out.println("No S1 found from L2");
        }

        // Load the service S1 from C2
        ServiceLoader<?> loaderC2 = ServiceLoader.load(S1Class, C2);
        loaderC2.iterator().forEachRemaining(System.out::println);
        Optional<?> first = loaderC2.findFirst();
        if(first.isPresent()) {
            System.out.println("Unexpected service found: "+first.get());
            // Where is this from?
            Module module = first.get().getClass().getModule();
            System.out.printf("S1.module: ", module);
            System.out.printf("%s/%s, layer: %s\n", module, module.hashCode(), module.getLayer());
        } else {
            System.out.println("No S1 found from C2");
        }
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

    ModuleLayer createModuleLayer(Path modulePath) {
        return createModuleLayer(modulePath, ModuleLayer.boot());
    }

    /**
     * Create a new ModuleLayer with the given modulePath and parent layer. This will use the ModuleFinder#ofSystem()
     * as the first argument to the resolve() method to avoid creating duplicate modules that cannot read themselves.
     * @param modulePath - the path to the module to load
     * @param parent - the parent layer to use
     * @return the new ModuleLayer
     */
    ModuleLayer createModuleLayer(Path modulePath, ModuleLayer parent) {
        Assertions.assertTrue(modulePath.toFile().exists(), modulePath.toAbsolutePath()+" exists");
        ModuleFinder finder = ModuleFinder.of(modulePath);
        Set<String> roots = finder.findAll()
                .stream()
                .map(m -> m.descriptor().name())
                .collect(Collectors.toSet());

        /* Use the ModuleFinder.ofSystem() to resolve modules loaded from the module-path to avoid
            creating duplicate modules that cannot read themselves.
        */
        Configuration theConfig = parent.configuration().resolve(ModuleFinder.ofSystem(), finder, roots);
        ModuleLayer.Controller theController = ModuleLayer
                .defineModulesWithOneLoader(theConfig, List.of(parent), null);
        ModuleLayer theLayer = theController.layer();
        return theLayer;
    }

    /**
     * Create a new ModuleLayer with the given modulePath and parent layer. This will use the ModuleFinder#ofSystem()
     * @param modulePath - the path to the module to load
     * @param parent - the parent layer to use
     * @return the new ModuleLayer
     */
    ModuleLayer createIsolatedModuleLayer(Path modulePath, ModuleLayer parent) {
        Assertions.assertTrue(modulePath.toFile().exists(), modulePath.toAbsolutePath()+" exists");
        ModuleFinder finder = ModuleFinder.of(modulePath);
        Set<String> roots = finder.findAll()
                .stream()
                .map(m -> m.descriptor().name())
                .collect(Collectors.toSet());

        // Only use the ModuleFinder for the modulePath
        Configuration theConfig = parent.configuration().resolve(finder, ModuleFinder.of(), roots);
        ModuleLayer.Controller theController = ModuleLayer
                .defineModulesWithOneLoader(theConfig, List.of(parent), null);
        ModuleLayer theLayer = theController.layer();
        return theLayer;
    }

}
