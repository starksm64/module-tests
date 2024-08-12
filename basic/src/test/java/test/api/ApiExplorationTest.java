package test.api;

import org.junit.jupiter.api.Test;

import java.lang.module.ModuleDescriptor;

import java.util.logging.Logger;

public class ApiExplorationTest {
    static final Logger log = Logger.getLogger(ApiExplorationTest.class.getName());

    @Test
    public void testDefaults() {
        Module module = ApiExplorationTest.class.getModule();
        System.out.println("Module.name: " + module.getName());
        System.out.println("Module.name: " + module.getName());
        System.out.println("Module.classLoader: " + module.getClassLoader());
        System.out.println("Module.packages: " + module.getPackages());
        System.out.println("Module.layer: " + module.getLayer());
        ModuleDescriptor descriptor = module.getDescriptor();
        System.out.println("Module.descriptor: " + descriptor);
        if(descriptor != null) {
            System.out.println("Module.descriptor.name: " + descriptor.name());
            System.out.println("Module.descriptor.name+version: " + descriptor.toNameAndVersion());
            System.out.println("Module.descriptor.isOopen: " + descriptor.isOpen());
            System.out.println("Module.descriptor.accessFlags: " + descriptor.accessFlags());
            System.out.println("Module.descriptor.requires: ");
            for (ModuleDescriptor.Requires require : descriptor.requires()) {
                System.out.printf("  %s%s", require.name(), require.accessFlags());
            }
        }

        log.info(module.toString());
    }
}
