package tag.jboss.modules.basic.provider;

import tag.jboss.modules.basic.spi.BService;

public class ProviderOfBService implements BService {
    public void doWork() {
        System.out.println("ProviderOfBService.doWork");
    }
}
