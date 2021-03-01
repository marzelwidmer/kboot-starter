package  ch.keepcalm.security.autoconfig

import ch.keepcalm.security.SecurityService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnMissingBean(SecurityService::class)
class SecurityServiceConfiguration {

    @Bean
    fun securityService() = SecurityService()
}
