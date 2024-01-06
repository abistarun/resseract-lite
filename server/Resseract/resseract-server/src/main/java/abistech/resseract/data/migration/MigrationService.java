package abistech.resseract.data.migration;

import abistech.resseract.util.Constants;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;

import java.util.Properties;

public class MigrationService {

    private MigrationService() {
    }

    public static void doMigration(Properties properties) {
        FluentConfiguration configuration = Flyway.configure();
        configuration.dataSource(properties.getProperty(Constants.DB_URL), properties.getProperty(Constants.DB_USER), properties.getProperty(Constants.DB_PASSWORD));
        configuration.locations("classpath:db/migration");

        Flyway flyway = configuration.load();
        flyway.migrate();
    }
}
