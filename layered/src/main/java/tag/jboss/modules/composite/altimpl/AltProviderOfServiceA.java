package tag.jboss.modules.composite.altimpl;

import tag.jboss.modules.basic.spi.AService;

public class AltProviderOfServiceA implements AService {
    @Override
    public void doWork() {
        System.out.println("AltProviderOfServiceA");
    }
}
