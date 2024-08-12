package tag.jboss.auto.provider;

import tag.jboss.auto.spi.LeakyLegacyService;

import java.sql.SQLException;
import java.util.Properties;

public class ProviderOfLeakyLegacyService implements LeakyLegacyService {

    @Override
    public Properties loadConfig() throws SQLException {
        return System.getProperties();
    }
}
