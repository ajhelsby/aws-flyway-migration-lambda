package com.ajhelsby.migrator.controller;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.rp.data.core.migrator.config.FlywayConfig;
import com.rp.data.core.migrator.model.ApiGatewayResponse;
import com.rp.data.core.migrator.model.DBType;
import com.rp.data.core.migrator.model.MigrationResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;

/**
 * Handler class for running a flyway migration.
 */
public class MigrationHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    static final Logger LOGGER = LogManager.getLogger(MigrationHandler.class);

    /**
     * Inherited method from RequestHandler to run a serverless request.
     *
     * @param input   map provided by serverless request
     * @param context aws information on serverless request
     * @return an ApiGatewayResponse object with either a failure or success
     */
    @Override
    final public ApiGatewayResponse handleRequest(final Map<String, Object> input, final Context context) {
        LOGGER.info("Starting db schema migration");

        LOGGER.info("Attempting to migrate master db");
        FlywayConfig masterFlywayConfig = new FlywayConfig(DBType.MASTER);
        Flyway masterFlyway = masterFlywayConfig.configure();
        masterFlyway.migrate();
        LOGGER.info("Successfully migrated master db");

        LOGGER.info("Attempting to migrate staging db");
        FlywayConfig stagingFlywayConfig = new FlywayConfig(DBType.STAGING);
        Flyway stagingFlyway = stagingFlywayConfig.configure();
        stagingFlyway.migrate();
        LOGGER.info("Successfully migrated staging db");

        return MigrationResponses.successfulResponse("Successfully migrated db", input);
    }
}
