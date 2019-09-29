package com.ajhelsby.migrator.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

import java.util.Optional;

/**
 * Class to configure the set up  of Flyway master_migration.
 */
public class FlywayConfig {

    static final Logger LOGGER = LogManager.getLogger(FlywayConfig.class);

    private static final String FLYWAY_CONFIG_ERROR =
            "Unable to configure Flyway due to missing url, username or password";
    private static final String FLYWAY_LOCATION = "classpath:db/migration";
    private final Optional<String> url;
    private final Optional<String> db;
    private final Optional<String> username;
    private final Optional<String> password;

    /**
     * Constructor used for retrieving db flyway variables from environment.
     */
    public FlywayConfig() {
        this.url = Optional.ofNullable(System.getenv("DB_URL"));
        this.db = Optional.ofNullable(System.getenv("DB_NAME"));
        this.username = Optional.ofNullable(System.getenv("DB_USERNAME"));
        this.password = Optional.ofNullable(System.getenv("DB_PASSWORD"));
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
            return Flyway.configure()
                    .dataSource(dbUrl, this.username.get(), this.password.get())
                    .locations(FLYWAY_LOCATION)
                    .load();

        } else {
            throw new FlywayException(FLYWAY_CONFIG_ERROR);
        }
    }

}
