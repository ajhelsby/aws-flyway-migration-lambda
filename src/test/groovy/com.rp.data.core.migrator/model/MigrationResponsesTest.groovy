package com.rp.data.core.migrator.model

import spock.lang.Specification

class MigrationResponsesTest extends Specification {
	
	def "Should return a successful ApiGatewayResponse"() {
		when:
		def response = MigrationResponses.successfulResponse("message", [:])
		
		then:
		response.statusCode == 200
	}
}