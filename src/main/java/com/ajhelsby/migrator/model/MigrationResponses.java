package com.rp.data.core.migrator.model;

import java.util.Collections;
import java.util.Map;

/**
 * Contains standardised responses to a migration request.
 */
final public class MigrationResponses {

    /**
     * Not called constructor.
     */
    private MigrationResponses() {
        //not called
    }

    /**
     * Method to retrieve a standard failed migration.
     * @param message a successful migration message
     * @param input initial input provided to the serverless function
     * @return an ApiGatewayResponse with a defaulted status code of 200 Ok
     */
    public static ApiGatewayResponse successfulResponse(final String message, final Map<String, Object> input) {
        Response responseBody = new Response(message, input);
        return ApiGatewayResponse.builder()
                .setObjectBody(responseBody)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }
}
