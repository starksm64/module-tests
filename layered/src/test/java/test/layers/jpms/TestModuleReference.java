package test.layers.jpms;

import java.io.IOException;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.net.URI;

public class TestModuleReference extends ModuleReference {
    public TestModuleReference(ModuleDescriptor descriptor, URI location) {
        super(descriptor, location);
    }

    @Override
    public ModuleReader open() throws IOException {
        return new TestModuleReader();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[module ");
        sb.append(descriptor().name());
        sb.append(", location=");
        sb.append(location().get());
        sb.append("]");
        return sb.toString();
    }
}
