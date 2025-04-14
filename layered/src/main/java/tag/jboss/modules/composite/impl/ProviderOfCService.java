package tag.jboss.modules.composite.impl;

import tag.jboss.modules.basic.spi.AService;
import tag.jboss.modules.basic.spi.BService;
import tag.jboss.modules.composite.api.CService;

import java.util.ServiceLoader;

public class ProviderOfCService implements CService {
    @Override
    public void doCompositeWork() {
        AService aService = getAService();
        BService bService = getBService();
        aService.doWork();
        bService.doWork();
    }
    @Override
    public void doCompositeWorkCL(ClassLoader classLoader) {
        AService aService = getAServiceCL(classLoader);
        BService bService = getBServiceCL(classLoader);
        aService.doWork();
        bService.doWork();
    }


    private AService getAService() {
        ServiceLoader<AService> serviceLoader = ServiceLoader.load(AService.class);
        return serviceLoader.findFirst().orElseThrow(() -> new RuntimeException("AService not found"));
    }
    private AService getAServiceCL(ClassLoader classLoader) {
        ServiceLoader<AService> serviceLoader = ServiceLoader.load(AService.class, classLoader);
        return serviceLoader.findFirst().orElseThrow(() -> new RuntimeException("AService not found"));
    }
    private BService getBService() {
        ServiceLoader<BService> serviceLoader = ServiceLoader.load(BService.class);
        return serviceLoader.findFirst().orElseThrow(() -> new RuntimeException("BService not found"));
    }
    private BService getBServiceCL(ClassLoader classLoader) {
        ServiceLoader<BService> serviceLoader = ServiceLoader.load(BService.class, classLoader);
        return serviceLoader.findFirst().orElseThrow(() -> new RuntimeException("BService not found"));
    }
}
