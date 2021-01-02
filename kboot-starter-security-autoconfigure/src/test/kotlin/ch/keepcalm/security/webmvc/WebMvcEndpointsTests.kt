package ch.keepcalm.security.webmvc

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

    companion object {
        private const val JWT_TOKEN_USER = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhZjM1ZjY0ZS1hMjFjLTQxNmUtODE4OC1iMjUxZjZhYmY1MGIiLCJzdWIiOiJqb2huLmRvZUBrZWVwY2FsbS5jaCIsImlhdCI6MTYwOTIyNjM3OSwiZXhwIjoxNjA5NTcxMjcyOTksImlzcyI6IktlZXBjYWxtIEF1dGggVGVzdCIsImF1ZCI6IktlZXBjYWxtIFRlc3QiLCJsYW5ndWFnZSI6ImVuIiwibGFzdG5hbWUiOiJEb2UiLCJmaXJzdG5hbWUiOiJKb2huIiwiZW1haWwiOiJqb2guZG9lQGtlZXBjYWxtLmNoIiwicm9sZXMiOiJrZWVwY2FsbS51c2VyIn0.yjch1wvNM_3Ix5JbPlfIiwW2qIJ9Vxw5WF8bV7DA9oU"
        private const val JWT_TOKEN_ADMIN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhZjM1ZjY0ZS1hMjFjLTQxNmUtODE4OC1iMjUxZjZhYmY1MGIiLCJzdWIiOiJqb2huLmRvZUBrZWVwY2FsbS5jaCIsImlhdCI6MTYwOTIyNjM3OSwiZXhwIjoxNjA5NTcxMjcyOTksImlzcyI6IktlZXBjYWxtIEF1dGggVGVzdCIsImF1ZCI6IktlZXBjYWxtIFRlc3QiLCJsYW5ndWFnZSI6ImVuIiwibGFzdG5hbWUiOiJEb2UiLCJmaXJzdG5hbWUiOiJKb2huIiwiZW1haWwiOiJqb2guZG9lQGtlZXBjYWxtLmNoIiwicm9sZXMiOiJrZWVwY2FsbS5hZG1pbiJ9.rLT3MIsIgpT-sqg8ihMtmAnQoiLntkvoVa972IYOhS0"
    }

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
