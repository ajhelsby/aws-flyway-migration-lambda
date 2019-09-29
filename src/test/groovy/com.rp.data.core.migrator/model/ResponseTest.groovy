package com.ajhelsby.migrator.model

import spock.lang.Specification

class ResponseTest extends Specification {
	
	def "Should create a response object with set values"() {
		expect:
		def response = new Response(message, map)
		response.message == message
		response.input == map
		
		where:
		message   | map
		""        | null
		"Sucess"  | null
		"Failed"  | null
		"Message" | [:]
		"Message" | ["Key": "Value"]
		
	}
}
