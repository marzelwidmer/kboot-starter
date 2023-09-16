@file:JvmName("globalSecurityCoFn")
package ch.keepcalm.security

import ch.keepcalm.security.user.CustomUserDetails
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext

suspend fun securityPrincipalCo() : CustomUserDetails = ReactiveSecurityContextHolder.getContext()
    .map { securityContext: SecurityContext ->
        securityContext.authentication.principal as CustomUserDetails
    }.awaitSingle()

suspend fun securityCurrentUserUserNameCo(): String = securityPrincipalCo().username
suspend fun securityCurrentUserPortalAccountIdCo(): String = securityPrincipalCo().username
suspend fun securityCurrentUserLanguageCo(): String = securityPrincipalCo().language

suspend fun securityCredentialsCo(): Any = ReactiveSecurityContextHolder.getContext()
    .map { securityContext: SecurityContext ->
        securityContext.authentication.credentials
    }.awaitSingle()


suspend fun getBearerFromSecurityContextCo(): String = "Bearer ${securityCredentialsCo()}"
