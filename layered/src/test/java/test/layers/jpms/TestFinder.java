package test.layers.jpms;

import java.io.IOException;
import java.lang.module.FindException;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public class TestFinder implements ModuleFinder {
    @Override
    public Optional<ModuleReference> find(String name) {
        if(name.equals("tag.jboss.layered")) {
            try {
                TestModuleReference mr = load();
                return Optional.of(mr);
            } catch (IOException e) {
                throw new FindException(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public Set<ModuleReference> findAll() {
        try {
            TestModuleReference mr = mr = load();
            return Set.of(mr);
        } catch (IOException e) {
            throw new FindException(e);
        }
    }

    private TestModuleReference load() throws IOException {
        Path infoPath = Path.of("target/classes/module-info.class");
        byte[] moduleInfo = Files.readAllBytes(infoPath);
        ModuleDescriptor md = ModuleDescriptor.read(ByteBuffer.wrap(moduleInfo));
        TestModuleReference ref = new TestModuleReference(md, infoPath.getParent().toUri());
        return ref;
    }
}
