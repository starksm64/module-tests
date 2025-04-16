package tag.jboss.legacy.provider;

import tag.jboss.legacy.spi.LeakyLegacyService;

import java.sql.SQLException;
import java.util.Properties;

public class ProviderOfLeakyLegacyService implements LeakyLegacyService {

    @Override
    public Properties loadConfig() throws SQLException {
        return System.getProperties();
    }
}
