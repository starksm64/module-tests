package test.layers.jpms;

import java.io.IOException;
import java.lang.module.ModuleReader;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class TestModuleReader implements ModuleReader {
    String[] content = {
            "module-info.class",
            "tag/",
            "tag/jboss/",
            "tag/jboss/modules/",
            "tag/jboss/modules/composite/",
            "tag/jboss/modules/composite/impl/",
            "tag/jboss/modules/composite/impl/ProviderOfCService.class",
            "tag/jboss/modules/composite/api/",
            "tag/jboss/modules/composite/api/CService.class",
            "tag/jboss/modules/composite/factory/",
            "tag/jboss/modules/composite/factory/Loader.class",
            "tag/jboss/modules/composite/altimpl/",
            "tag/jboss/modules/composite/altimpl/AltProviderOfServiceA.class"
    };
    @Override
    public Optional<URI> find(String name) throws IOException {
        if(name.equals("tag.jboss.layered")) {
            return Optional.of(URI.create("file://./target/classes"));
        }
        return Optional.empty();
    }

    @Override
    public Stream<String> list() throws IOException {
        return Arrays.stream(content);
    }

    @Override
    public void close() throws IOException {

    }
}
