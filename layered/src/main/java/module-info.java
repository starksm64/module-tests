module tag.jboss.layered {
    exports tag.jboss.modules.composite.api;

    requires tag.jboss.basic;

    uses tag.jboss.modules.basic.spi.AService;
    uses tag.jboss.modules.basic.spi.BService;

    provides tag.jboss.modules.basic.spi.AService
        with tag.jboss.modules.composite.altimpl.AltProviderOfServiceA;
    provides tag.jboss.modules.composite.api.CService
        with tag.jboss.modules.composite.impl.ProviderOfCService;
}