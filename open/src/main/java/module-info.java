/**
 * This module exports all module and opens them for reflection.
 */
open module tag.jboss.open {
    exports tag.jboss.modules.open.api;
    exports tag.jboss.modules.open.spi;
    exports tag.jboss.modules.open.impl;
    provides tag.jboss.modules.open.spi.SerializationService with tag.jboss.modules.open.impl.ProviderOfSerializationService;
}