package ch.keepcalm.security.jwt

import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@EnableAutoConfiguration
@SpringBootTest(classes = [SecurityJwtConfigurer::class])
@ActiveProfiles("test")
class SecurityJwtConfigurerTests {

    @Autowired
    private lateinit var securityJwtConfigurer: SecurityJwtConfigurer

    @Test
    fun `should give back the issuer from the test configuration`() {
        securityJwtConfigurer.issuer `should be equal to` "Keepcalm Auth Test"
    }

    @Test
    fun `should give back the audience from the test configuration`() {
        securityJwtConfigurer.audience `should be equal to` "Keepcalm Test"
    }

    @Test
    fun `should give back the secret from the test configuration`() {
        val decodedSecret: ByteArray = Base64.getDecoder().decode(securityJwtConfigurer.secret)
        String(decodedSecret) `should be equal to` "s3cretP@ssw0rd"
    }
}
