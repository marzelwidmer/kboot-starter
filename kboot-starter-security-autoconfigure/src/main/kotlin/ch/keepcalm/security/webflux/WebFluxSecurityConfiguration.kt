package ch.keepcalm.security.webflux

import ch.keepcalm.security.ROLE_KEEPCALM_ADMIN
import ch.keepcalm.security.ROLE_KEEPCALM_USER
import ch.keepcalm.security.endpoint.SecurityEndpointsConfigurer
import ch.keepcalm.security.jwt.JwtTokenVerifier
import ch.keepcalm.security.jwt.SecurityJwtConfigurer
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository

@Configuration
@EnableWebFluxSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableConfigurationProperties(SecurityEndpointsConfigurer::class, SecurityJwtConfigurer::class, SecurityProperties::class)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
class WebFluxSecurityConfiguration(private val securityEndpointsConfigurer: SecurityEndpointsConfigurer) {

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity, apiAuthenticationWebFilter: AuthenticationWebFilter): SecurityWebFilterChain {
        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .addFilterAt(apiAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .authorizeExchange()
            .pathMatchers(*securityEndpointsConfigurer.getAdminEndpoints()).hasAuthority(ROLE_KEEPCALM_ADMIN)
            .pathMatchers(*securityEndpointsConfigurer.getUserEndpoints()).hasAnyAuthority(ROLE_KEEPCALM_ADMIN, ROLE_KEEPCALM_USER)
            .pathMatchers(*securityEndpointsConfigurer.getUnsecuredEndpoints()).permitAll()
            .anyExchange().authenticated()
        return http.build()
    }

    @Bean
    fun apiAuthenticationWebFilter(
        jwtAuthenticationConverter: JwtAuthenticationConverter
    ): AuthenticationWebFilter {
        val apiAuthenticationWebFilter = AuthenticationWebFilter(JwtAuthenticationManager())
        apiAuthenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter)
        apiAuthenticationWebFilter.setSecurityContextRepository(securityContextRepository())
        return apiAuthenticationWebFilter
    }

    @Bean
    fun jwtAuthenticationConverter(jwtTokenVerifier: JwtTokenVerifier): JwtAuthenticationConverter =
        JwtAuthenticationConverter(jwtTokenVerifier)

    @Bean
    fun jwtTokenVerifier(securityJwtConfigurer: SecurityJwtConfigurer): JwtTokenVerifier = JwtTokenVerifier(securityJwtConfigurer)

    @Bean
    fun securityContextRepository(): NoOpServerSecurityContextRepository = NoOpServerSecurityContextRepository.getInstance()
}
