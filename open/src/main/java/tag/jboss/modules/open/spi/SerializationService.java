package tag.jboss.modules.open.spi;

public interface SerializationService {
    String serialize(Object object);
    Object deserialize(String string);
}
