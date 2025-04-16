package test.resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tag.jboss.modules.basic.spi.AService;

import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.net.URL;
import java.util.Optional;

public class ResourceLoadingTest {
    @Test
    public void testConfURLViaCL() throws IOException {
        Module basicModule = AService.class.getModule();
        System.out.println("testLoadResourceCL: "+basicModule);
        URL configPropsURL = AService.class.getResource("/config.properties");
        System.out.println("configProps(/config.properties) = " + configPropsURL);
        configPropsURL = AService.class.getResource("/META-INF/config.properties");
        System.out.println("configProps(/META-INF/config.properties) = " + configPropsURL);
        configPropsURL = AService.class.getResource("/props/config.properties");
        System.out.println("configProps(/props/config.properties) = " + configPropsURL);

        ClassLoader classLoader = AService.class.getClassLoader();
        configPropsURL = classLoader.getResource("config.properties");
        System.out.println("CL.configProps(config.properties) = " + configPropsURL);
        configPropsURL = classLoader.getResource("META-INF/config.properties");
        System.out.println("CL.configProps(META-INF/config.properties) = " + configPropsURL);
        configPropsURL = classLoader.getResource("props/config.properties");
        System.out.println("CL.configProps(props/config.properties) = " + configPropsURL);
    }
    @Test
    public void testLoadConfigPropsML() throws IOException {
        Module testModule = getClass().getModule();
        System.out.println("testLoadResourceCL: "+testModule);
        try(InputStream is = testModule.getResourceAsStream("/config.properties")) {
            System.out.println("testModule getResourceAsStream: " + is);
        }
    }
    @Test
    public void testLoadConfigPropsInPropsDirML() throws IOException {
        Module testModule = getClass().getModule();
        try(InputStream is = testModule.getResourceAsStream("/props/config.properties")) {
            System.out.println("testModule getResourceAsStream: " + is);
            ModuleReference moduleRef = testModule.getLayer()     // ModuleLayer
                    .configuration()              // Configuration
                    .findModule(testModule.getName()) // Optional<ResolvedModule>
                    .orElseThrow()                // ResolvedModule
                    .reference();                 // ModuleReference
            listModule(moduleRef);
        }
        Module basicModule = AService.class.getModule();
        try(InputStream is = basicModule.getResourceAsStream("/props/config.properties")) {
            System.out.println("basicModule getResourceAsStream: " + is);
            ModuleReference moduleRef = testModule.getLayer()     // ModuleLayer
                    .configuration()              // Configuration
                    .findModule(basicModule.getName()) // Optional<ResolvedModule>
                    .orElseThrow()                // ResolvedModule
                    .reference();
            listModule(moduleRef);
        }
    }

    private void listModule(String name) throws IOException {
        Optional<ModuleReference> basicOpt = ModuleFinder.ofSystem().find(name);
        ModuleReference moduleRef = basicOpt.orElseThrow();
        listModule(moduleRef);
    }
    private void listModule(ModuleReference moduleRef) throws IOException {
        System.out.printf("%s module contents: ", moduleRef.descriptor().name());
        try (var reader = moduleRef.open()) {
            reader.list().forEach(System.out::println);
        }
    }
}
