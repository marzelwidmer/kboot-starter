package ch.keepcalm.security.webflux

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import reactor.core.publisher.Mono

class JwtAuthenticationManager : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        if (authentication is PreAuthenticatedAuthenticationToken) {
            return Mono.just(authentication)
        }
        throw BadCredentialsException("Unsupported authentication token: $authentication")
    }
}
