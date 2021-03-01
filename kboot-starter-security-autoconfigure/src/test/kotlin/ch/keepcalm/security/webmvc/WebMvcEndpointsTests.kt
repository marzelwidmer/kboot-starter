package ch.keepcalm.security.webmvc

import ch.keepcalm.security.JWT_TOKEN_ADMIN
import ch.keepcalm.security.JWT_TOKEN_USER
import ch.keepcalm.security.mock.TestController
import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldNotBeNull
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(properties = ["spring.main.web-application-type=servlet"], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(value = [TestController::class])
@AutoConfigureWebTestClient
class WebMvcEndpointsTests(@Autowired private val testRestTemplate: TestRestTemplate) {

    @Test
    fun `Test should give back HTTP 200 for unsecure WebMvc endpoint`() {
        val response: ResponseEntity<String> = testRestTemplate.getForEntity("/unsecure", String::class.java)
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        response.statusCode `should be` HttpStatus.OK
    }

    @Test
    fun `Test should give back HTTP 403 Forbidden without JWT for protected WebMvc endpoint`() {
        val response: ResponseEntity<String> = testRestTemplate.getForEntity("/api/user/foo", String::class.java)
        response.statusCode `should be` HttpStatus.FORBIDDEN
    }

    @Test
    fun `Test should give back HTTP 200 for ROLE User protected WebMvc endpoint `() {
        val url = "/api/user/foo"

        val requestHeaders = HttpHeaders()
        requestHeaders.setBearerAuth(JWT_TOKEN_USER)
        val request: HttpEntity<String> = HttpEntity(requestHeaders)
        val response: ResponseEntity<String> = testRestTemplate.exchange(url, HttpMethod.GET, request, String::class.java)

        response.shouldNotBeNull()
        response.statusCode `should be` HttpStatus.OK
    }

    @Test
    fun `Test should give back HTTP 200 for ROLE Admin protected WebMvc endpoint `() {
        val url = "/api/admin"

        val requestHeaders = HttpHeaders()
        requestHeaders.setBearerAuth(JWT_TOKEN_ADMIN)
        val request: HttpEntity<String> = HttpEntity(requestHeaders)
        val response: ResponseEntity<String> = testRestTemplate.exchange(url, HttpMethod.GET, request, String::class.java)

        response.shouldNotBeNull()
        response.statusCode `should be` HttpStatus.OK
    }
}
