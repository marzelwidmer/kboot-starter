package ch.keepcalm.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import java.nio.charset.StandardCharsets
import java.util.*

@EnableConfigurationProperties(JwtSecurityConfigurer::class)
@ConfigurationProperties("keepcalm.security.jwt")
class JwtSecurityConfigurer(var issuer: String? = "Keepcalm Auth", var audience: String? = "Keepcalm") {
    var secret: String = ""
        set(value) {
            field = Base64.getEncoder().encodeToString(value.toByteArray(StandardCharsets.UTF_8))
        }
}
