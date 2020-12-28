package ch.keepcalm.security.web

import ch.keepcalm.security.jwt.JwtSecurityConfigurer
import ch.keepcalm.security.jwt.JwtTokenFilter
import ch.keepcalm.security.jwt.JwtTokenVerifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(WebSecurityEndpointConfigurer::class, JwtSecurityConfigurer::class, SecurityProperties::class)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
class WebSecurityConfiguration(
    private val webSecurityEndpointConfigurer: WebSecurityEndpointConfigurer,
    private val jwtSecurityConfigurer: JwtSecurityConfigurer,
    private val securityProperties: SecurityProperties
) : WebSecurityConfigurerAdapter() {

    val log: Logger = LoggerFactory.getLogger(WebSecurityConfiguration::class.java)

    companion object{
        const val ROLE_ADMIN = "keepcalm.admin"
        const val ROLE_USER = "keepcalm.user"
        const val ROLE_ACTUATOR = "ACTUATOR"
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
        http
            .addFilterBefore(JwtTokenFilter(JwtTokenVerifier(jwtSecurityConfigurer)), UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement().sessionCreationPolicy(STATELESS).and()
            .authorizeRequests()
            .antMatchers(*getUserEndpoints()).hasAnyAuthority(ROLE_ADMIN)
            .antMatchers(*getAdminEndpoints()).hasAnyAuthority(ROLE_USER)
            .antMatchers(*getUnsecuredEndpoints()).permitAll()
            .requestMatchers(EndpointRequest.to(HealthEndpoint::class.java, InfoEndpoint::class.java)).permitAll()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAnyRole(*getAdminRoles(securityProperties).toTypedArray())
    }

    private fun getUserEndpoints() = webSecurityEndpointConfigurer.user.toTypedArray()
        .also {
            log.debug("Configure (User Endpoints) with [${it.joinToString()}]")
        }
    private fun getAdminEndpoints() = webSecurityEndpointConfigurer.admin.toTypedArray()
        .also {
            log.debug("Configure (Admin Endpoints) with [${it.joinToString()}]")
        }
    private fun getUnsecuredEndpoints() = webSecurityEndpointConfigurer.unsecured.toTypedArray()
        .also {
            log.debug("Configure (Unsecured Endpoints) with [${it.joinToString()}]")
        }

    private fun getAdminRoles(securityProperties: SecurityProperties) = if (securityProperties.user.roles.isNotEmpty()) securityProperties.user.roles else listOf("ROLE_ACTUATOR")

}
