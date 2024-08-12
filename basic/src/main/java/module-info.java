/**
 * This module provides basic API.
 */
module tag.jboss.basic {
    exports tag.jboss.modules.basic.api;
    exports tag.jboss.modules.basic.spi;
    provides tag.jboss.modules.basic.spi.AService with tag.jboss.modules.basic.provider.ProviderOfAService;
}