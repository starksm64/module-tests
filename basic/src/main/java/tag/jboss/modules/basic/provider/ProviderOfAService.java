package tag.jboss.modules.basic.provider;

import tag.jboss.modules.basic.spi.AService;

public class ProviderOfAService implements AService {
    public void doWork() {
        System.out.println("ProviderOfAService.doWork");
    }
}
