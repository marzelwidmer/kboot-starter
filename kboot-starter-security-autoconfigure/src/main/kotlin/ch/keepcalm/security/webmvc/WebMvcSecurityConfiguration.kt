package ch.keepcalm.security.webmvc

import ch.keepcalm.security.ROLE_ACTUATOR
import ch.keepcalm.security.ROLE_KEEPCALM_ADMIN
import ch.keepcalm.security.ROLE_KEEPCALM_USER
import ch.keepcalm.security.authz.AccessDecisionVoterConfigurer
import ch.keepcalm.security.authz.OPAVoter
import ch.keepcalm.security.endpoint.SecurityEndpointsConfigurer
import ch.keepcalm.security.jwt.JwtTokenVerifier
import ch.keepcalm.security.jwt.SecurityJwtConfigurer
import org.slf4j.LoggerFactory
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.vote.AuthenticatedVoter
import org.springframework.security.access.vote.RoleVoter
import org.springframework.security.access.vote.UnanimousBased
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.access.expression.WebExpressionVoter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(SecurityEndpointsConfigurer::class, SecurityJwtConfigurer::class, SecurityProperties::class, AccessDecisionVoterConfigurer::class)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
class WebMvcSecurityConfiguration(
    private val securityEndpointsConfigurer: SecurityEndpointsConfigurer,
    private val accessDecisionVoterConfigurer: AccessDecisionVoterConfigurer,
    private val securityJwtConfigurer: SecurityJwtConfigurer,
    private val configurableEnvironment: ConfigurableEnvironment,
    private val securityProperties: SecurityProperties
) : WebSecurityConfigurerAdapter() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Order(1)
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)

        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .addFilterBefore(JwtTokenFilter(JwtTokenVerifier(securityJwtConfigurer)), UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement().sessionCreationPolicy(STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(*securityEndpointsConfigurer.adminEndPoints).hasAuthority(
                ROLE_KEEPCALM_ADMIN
            )
            .antMatchers(*securityEndpointsConfigurer.userEndPoints).hasAnyAuthority(
                ROLE_KEEPCALM_ADMIN,
                ROLE_KEEPCALM_USER
            )
            .antMatchers(*securityEndpointsConfigurer.unsecureEndPoints).permitAll()
            .requestMatchers(EndpointRequest.to(HealthEndpoint::class.java, InfoEndpoint::class.java)).permitAll()
            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAnyRole(*getAdminRoles(securityProperties).toTypedArray())
        configureOpa(http)
    }

    private fun configureOpa(http: HttpSecurity) {
        if (configurableEnvironment.getProperty("keepcalm.security.access-decision-voter.voters").equals("OPA")) {
            configureOpaAccessDecisionManager(http)
        }
    }

    private fun getAdminRoles(securityProperties: SecurityProperties) = if (securityProperties.user.roles.isNotEmpty()) securityProperties.user.roles else listOf(ROLE_ACTUATOR)

    fun configureOpaAccessDecisionManager(http: HttpSecurity): ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry? {
        return http.authorizeRequests().anyRequest().authenticated().accessDecisionManager(accessDecisionManager())
    }

    @ConditionalOnProperty(prefix = "keepcalm.security", name = ["access-decision-voter.voters"], havingValue = "OPA")
    @Bean
    fun accessDecisionManager(): AccessDecisionManager {
        val opaEndPoint = accessDecisionVoterConfigurer.url
        val decisionVoters: List<AccessDecisionVoter<out Any?>> = listOf(
            WebExpressionVoter(),
            RoleVoter(),
            AuthenticatedVoter(),
            OPAVoter(opaEndPoint)
        )
        log.debug(
            """

            ------> Configure Open Policy Agent  <------ 
            ------> $opaEndPoint  <------
            """.trimIndent()
        )
        return UnanimousBased(decisionVoters)
    }
}
