package ch.keepcalm.security.webmvc

import ch.keepcalm.security.ROLE_KEEPCALM_ADMIN
import ch.keepcalm.security.ROLE_KEEPCALM_USER
import ch.keepcalm.security.endpoint.SecurityEndpointsConfigurer
import ch.keepcalm.security.jwt.JwtTokenVerifier
import ch.keepcalm.security.jwt.SecurityJwtConfigurer
import jakarta.annotation.PostConstruct
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableWebSecurity
@EnableConfigurationProperties(SecurityEndpointsConfigurer::class, SecurityJwtConfigurer::class, SecurityProperties::class)
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
class WebMvcSecurityConfiguration(
    private val securityEndpointsConfigurer: SecurityEndpointsConfigurer,
    private val securityJwtConfigurer: SecurityJwtConfigurer,
    private val securityProperties: SecurityProperties,
) : ch.keepcalm.security.WebSecurityConfiguration() {

    @PostConstruct
    fun setup() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity, introspector: HandlerMappingIntrospector): SecurityFilterChain {
        val mvcRequestMatcherBuilder = MvcRequestMatcher.Builder(introspector)
        http
            .httpBasic(Customizer.withDefaults())
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .addFilterBefore(JwtTokenFilter(JwtTokenVerifier(securityJwtConfigurer)), UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement { it.sessionCreationPolicy(STATELESS) }
            .authorizeHttpRequests() {
                it
                    .requestMatchers(mvcRequestMatcherBuilder.pattern(INDEX_PATTERN)).permitAll()
                    .requestMatchers(mvcRequestMatcherBuilder.pattern(DOCUMENTATION_PATTERN)).permitAll()
                    .requestMatchers(*mapToMvcRequestMatchers(securityEndpointsConfigurer.adminEndPoints, mvcRequestMatcherBuilder)).hasAuthority(
                        ROLE_KEEPCALM_ADMIN,
                    )
                    .requestMatchers(*mapToMvcRequestMatchers(securityEndpointsConfigurer.userEndPoints, mvcRequestMatcherBuilder)).hasAnyAuthority(
                        ROLE_KEEPCALM_USER,
                    )
                    .requestMatchers(*mapToMvcRequestMatchers(securityEndpointsConfigurer.unsecureEndPoints, mvcRequestMatcherBuilder)).permitAll()
                    .requestMatchers(EndpointRequest.to(HealthEndpoint::class.java, InfoEndpoint::class.java)).permitAll()
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAnyRole(*getAdminRoles(securityProperties).toTypedArray())
                    .anyRequest().authenticated()
            }
        return http.build()
    }

    fun mapToMvcRequestMatchers(endpointPaths: Array<String>, mvcRequestMatcherBuilder: MvcRequestMatcher.Builder) : Array<MvcRequestMatcher>{
        return endpointPaths.map {
            mvcRequestMatcherBuilder.pattern(it)
        }.toTypedArray()
    }
}
