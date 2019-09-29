package com.ajhelsby.migrator.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Map;

/**
 * Dto object containing a json response object.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Response {

	private final String message;
	private final Map<String, Object> input;

	/**
	 * Constructor for creating a Response object with set fields.
	 * @param message the response message to the serverless function
	 * @param input initial input provided to the serverless function
	 */
	public Response(final String message, final Map<String, Object> input) {
		this.message = message;
		this.input = input;
	}
}
