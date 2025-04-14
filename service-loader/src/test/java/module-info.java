module serviceloader.test {
    exports mp;

    // The basic module
    requires tag.jboss.basic;
    requires tag.jboss.layered;

    // Testing
    requires transitive org.junit.jupiter.engine;
    requires transitive org.junit.jupiter.api;

    uses tag.jboss.modules.basic.spi.AService;
    uses tag.jboss.modules.basic.spi.BService;
    uses tag.jboss.modules.composite.api.CService;
}