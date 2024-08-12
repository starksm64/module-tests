package tag.jboss.modules.open.impl;

import tag.jboss.modules.open.spi.SerializationService;

public class ProviderOfSerializationService implements SerializationService {
    @Override
    public String serialize(Object object) {
        return "";
    }

    @Override
    public Object deserialize(String string) {
        return null;
    }
}
