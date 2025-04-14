package tag.jboss.modules.composite.api;

public interface CService {
    void doCompositeWork();
    void doCompositeWorkCL(ClassLoader classLoader);
}
