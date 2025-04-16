package tag.jboss.legacy.spi;

import java.sql.SQLException;
import java.util.Properties;

public interface LeakyLegacyService {
    public Properties loadConfig() throws SQLException;
}
