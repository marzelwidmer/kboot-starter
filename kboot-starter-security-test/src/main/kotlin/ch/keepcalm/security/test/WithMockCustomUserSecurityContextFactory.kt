@file:Suppress("Annotator")
package ch.keepcalm.security.test

import ch.keepcalm.security.user.CustomUserDetails
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockCustomUserSecurityContextFactory : WithSecurityContextFactory<WithMockCustomUser> {

    override fun createSecurityContext(customUser: WithMockCustomUser?): SecurityContext {
        val securityContext = SecurityContextHolder.createEmptyContext()
        securityContext.authentication = customUser?.let {
            val authorities: List<SimpleGrantedAuthority> = it.authorities.map {
                SimpleGrantedAuthority(it)
            }
            val principal =
                CustomUserDetails(
                    subject = it.username,
                    password = it.password,
                    authorities = authorities,
                    firstname = it.firstname,
                    lastname = it.lastname,
                    language = it.language
                )
            UsernamePasswordAuthenticationToken(principal, "password", principal.authorities)
        }
        return securityContext
    }
}
