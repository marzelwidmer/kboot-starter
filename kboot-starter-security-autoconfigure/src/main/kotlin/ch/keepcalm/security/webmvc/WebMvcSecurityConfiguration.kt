package ch.keepcalm.security.webmvc

import ch.keepcalm.security.ROLE_ACTUATOR
import ch.keepcalm.security.endpoint.SecurityEndpointsConfigurer
import ch.keepcalm.security.jwt.JwtTokenVerifier
import ch.keepcalm.security.jwt.SecurityJwtConfigurer
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
@EnableConfigurationProperties(SecurityEndpointsConfigurer::class, SecurityJwtConfigurer::class, SecurityProperties::class)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
class WebMvcSecurityConfiguration(
    private val securityEndpointsConfigurer: SecurityEndpointsConfigurer,
    private val securityJwtConfigurer: SecurityJwtConfigurer,
    private val securityProperties: SecurityProperties
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .addFilterBefore(JwtTokenFilter(JwtTokenVerifier(securityJwtConfigurer)), UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement().sessionCreationPolicy(STATELESS).and()
            .authorizeRequests()
            .antMatchers(*securityEndpointsConfigurer.getAdminEndpoints()).hasAuthority(ch.keepcalm.security.ROLE_KEEPCALM_ADMIN)
            .antMatchers(*securityEndpointsConfigurer.getUserEndpoints()).hasAnyAuthority(ch.keepcalm.security.ROLE_KEEPCALM_ADMIN, ch.keepcalm.security.ROLE_KEEPCALM_USER)
            .antMatchers(*securityEndpointsConfigurer.getUnsecuredEndpoints()).permitAll()
            .requestMatchers(EndpointRequest.to(HealthEndpoint::class.java, InfoEndpoint::class.java)).permitAll()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAnyRole(*getAdminRoles(securityProperties).toTypedArray())
            .anyRequest().authenticated()
    }

    private fun getAdminRoles(securityProperties: SecurityProperties) = if (securityProperties.user.roles.isNotEmpty()) securityProperties.user.roles else listOf(ROLE_ACTUATOR)
}
