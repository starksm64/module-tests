/**
 * This module provides basic API.
 */
module tag.jboss.basic {
    exports tag.jboss.modules.basic.api;
    exports tag.jboss.modules.basic.data;
    exports tag.jboss.modules.basic.spi;

    // If this is not open, /props/config.properties will not be found as resource
    opens props;

    provides tag.jboss.modules.basic.spi.AService with tag.jboss.modules.basic.provider.ProviderOfAService;
    provides tag.jboss.modules.basic.spi.BService with tag.jboss.modules.basic.provider.ProviderOfBService;

}
