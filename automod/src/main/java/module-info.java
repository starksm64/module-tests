open module tag.jboss.auto {
    exports tag.jboss.auto.api;
    exports tag.jboss.auto.spi;

    provides tag.jboss.auto.api.AutoPublicApi with tag.jboss.auto.impl.ImplOfAutoPublicAPI;
}