package ch.keepcalm.security.endpoint

import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest

@EnableAutoConfiguration
@SpringBootTest(classes = [SecurityEndpointsConfigurer::class])
class SecurityEndpointConfigurerDefaultTests {

    @Autowired
    private lateinit var securityEndpointsConfigurer: SecurityEndpointsConfigurer

    @Test
    fun `should give back the unsecured Endpoint from the test configuration`() {
        securityEndpointsConfigurer.unsecured shouldContainSame listOf<String>("/public**")
    }

    @Test
    fun `should give back the admin Endpoint from the test configuration`() {
        securityEndpointsConfigurer.admin shouldContainSame listOf<String>("/admin**")
    }

    @Test
    fun `should give back the user Endpoint from the test configuration`() {
        securityEndpointsConfigurer.user shouldContainAll listOf<String>("/api**")
    }
}
