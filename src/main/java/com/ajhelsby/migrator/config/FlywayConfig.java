package com.ajhelsby.migrator.config;

import com.rp.data.core.migrator.model.DBType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

import java.util.Optional;

/**
 * Class to configure the set up  of Flyway master_migration.
 */
public class FlywayConfig {

    private static final String FLYWAY_CONFIG_ERROR =
            "Unable to configure Flyway due to missing url, username or password";

    private static final String MASTER_FLYWAY_LOCATION = "classpath:db/master_migration";
    private static final String STAGING_FLYWAY_LOCATION = "classpath:db/staging_migration";

    static final Logger LOGGER = LogManager.getLogger(FlywayConfig.class);

    private final Optional<String> url;
    private final Optional<String> db;
    private final Optional<String> username;
    private final Optional<String> password;
    private final DBType dbType;

    /**
     * Constructor used for retrieving db flyway variables from environment.
     */
    public FlywayConfig(final DBType dbType) {
        this.dbType = dbType;
        if (dbType == DBType.MASTER) {

            this.url = Optional.ofNullable(System.getenv("MASTER_DB_URL"));
            this.db = Optional.ofNullable(System.getenv("MASTER_DB_NAME"));
            this.username = Optional.ofNullable(System.getenv("MASTER_DB_USERNAME"));
            this.password = Optional.ofNullable(System.getenv("MASTER_DB_PASSWORD"));

        } else if (dbType == DBType.STAGING) {
            this.url = Optional.ofNullable(System.getenv("STAGING_DB_URL"));
            this.db = Optional.ofNullable(System.getenv("STAGING_DB_NAME"));
            this.username = Optional.ofNullable(System.getenv("STAGING_DB_USERNAME"));
            this.password = Optional.ofNullable(System.getenv("STAGING_DB_PASSWORD"));

        } else {
            throw new FlywayException(String.format("The Database type %s does not exist", dbType));
        }
    }

    /**
     * Method for configuring flyway throws a runtime exception if missing db variables.
     *
     * @return an instance of the Flyway object
     */
    final public Flyway configure() {

        LOGGER.info("Setting up Flyway config");

        if (this.url.isPresent()
                && this.db.isPresent()
                && this.username.isPresent()
                && this.password.isPresent()) {
            String dbUrl = String.format("%s/%s", this.url.get(), this.db.get());

            LOGGER.info("Connecting to " + dbUrl);
            switch (this.dbType) {
                case MASTER:
                    return Flyway.configure()
                            .dataSource(dbUrl, this.username.get(), this.password.get())
                            .locations(MASTER_FLYWAY_LOCATION)
                            .load();
                case STAGING:
                    return Flyway.configure()
                            .dataSource(dbUrl, this.username.get(), this.password.get())
                            .locations(STAGING_FLYWAY_LOCATION)
                            .load();
                default: throw new FlywayException(String.format("The Database type %s does not exist", dbType));
            }

        } else {
            throw new FlywayException(FLYWAY_CONFIG_ERROR);
        }
    }

}
