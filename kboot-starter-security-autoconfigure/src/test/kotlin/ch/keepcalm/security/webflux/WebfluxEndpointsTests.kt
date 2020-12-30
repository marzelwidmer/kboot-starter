package ch.keepcalm.security.webflux

import ch.keepcalm.security.mock.TestController
import org.amshove.kluent.`should be`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

@SpringBootTest (properties = ["spring.main.web-application-type=reactive"], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(value = [TestController::class])
@AutoConfigureWebTestClient
class WebfluxEndpointsTests (@Autowired private val testRestTemplate: TestRestTemplate) {

	@Test
	fun `WebFlux unsecure endpoint should give back HTTP 200`() {
		val response: ResponseEntity<String> = testRestTemplate.getForEntity("/unsecure", String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
		response.statusCode `should be` HttpStatus.OK
	}

	@Test
	fun `WebFlux protected endpoint should give back HTTP 401 Unauthorized without JWT`() {
		val response: ResponseEntity<String> = testRestTemplate.getForEntity("/api/user/foo", String::class.java)
		response.statusCode `should be` HttpStatus.UNAUTHORIZED
	}

	@Test
	fun `WebFlux protected EndPoint should give back HTTP 401 Unauthorized without JWT`() {
		val response: ResponseEntity<String> = testRestTemplate.getForEntity("/secure" , String::class.java)
		response.statusCode `should be` HttpStatus.UNAUTHORIZED
	}

}