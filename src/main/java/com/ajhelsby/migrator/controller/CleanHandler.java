package com.ajhelsby.migrator.controller;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.rp.data.core.migrator.config.FlywayConfig;
import com.rp.data.core.migrator.model.ApiGatewayResponse;
import com.rp.data.core.migrator.model.DBType;
import com.rp.data.core.migrator.model.MigrationResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;

import java.util.Map;

/**
 * Handler class for cleaning the database and running a flyway migration.
 */
public class CleanHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    static final Logger LOGGER = LogManager.getLogger(CleanHandler.class);

    /**
     * Inherited method from RequestHandler to run a serverless request.
     *
     * @param input   map provided by serverless request
     * @param context aws information on serverless request
     * @return an ApiGatewayResponse object with either a failure or success
     */
    @Override
    final public ApiGatewayResponse handleRequest(final Map<String, Object> input, final Context context) {
        LOGGER.info("Starting db schema clean and migration");

        LOGGER.info("Attempting to clean and migrate master db");
        FlywayConfig masterFlywayConfig = new FlywayConfig(DBType.MASTER);
        Flyway masterFlyway = masterFlywayConfig.configure();
        masterFlyway.clean();
        masterFlyway.migrate();
        LOGGER.info("Successfully cleaned and migrated master db");

        LOGGER.info("Attempting to clean and migrate staging db");
        FlywayConfig stagingFlywayConfig = new FlywayConfig(DBType.STAGING);
        Flyway stagingFlyway = stagingFlywayConfig.configure();
        stagingFlyway.clean();
        stagingFlyway.migrate();
        LOGGER.info("Successfully cleaned ad migrated staging db");

        return MigrationResponses.successfulResponse("Successfully cleaned and migrated db", input);
    }
}

