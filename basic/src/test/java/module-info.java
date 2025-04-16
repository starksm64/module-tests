module test.tag.jboss.basic {
    exports test.api;
    exports test.resource;

    // The automatic named module
    requires tag.jboss.auto;
    // The basic module
    requires tag.jboss.basic;
    // The open module
    requires tag.jboss.open;
    // Junit
    requires transitive org.junit.jupiter.engine;
    requires transitive org.junit.jupiter.api;
    //
    requires java.sql;

    uses tag.jboss.modules.basic.spi.AService;
    uses tag.jboss.auto.spi.LeakyLegacyService;
    uses tag.jboss.modules.open.spi.SerializationService;
}