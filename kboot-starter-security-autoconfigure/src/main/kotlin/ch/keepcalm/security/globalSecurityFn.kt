@file:JvmName("globalSecurityFn")

package ch.keepcalm.security

import ch.keepcalm.security.user.CustomUserDetails
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

const val AUTHENTICATION_SCHEMA = "Bearer "
const val ROLE_KEEPCALM_ADMIN = "keepcalm.admin"
const val ROLE_KEEPCALM_USER = "keepcalm.user"
const val ROLE_ACTUATOR = "ACTUATOR"


fun securityPrincipal(): CustomUserDetails = SecurityContextHolder.getContext().authentication.principal as CustomUserDetails
fun securityIsPrincipalUser(): Boolean = true
fun securityCurrentUserBearerToken(): String = "Bearer ${SecurityContextHolder.getContext().authentication.credentials}"
fun securityGetAuthorities(): MutableCollection<out GrantedAuthority> = SecurityContextHolder.getContext().authentication.authorities
fun securityIsCurrentUserInRoleAdmin(): Boolean = securityGetRoles().contains(ROLE_KEEPCALM_ADMIN)
fun securityIsCurrentUserInRoleUser(): Boolean = securityGetRoles().contains(ROLE_KEEPCALM_USER)
fun securityCurrentUserUserName(): String = securityPrincipal().username

fun securityGetRoles(): List<String> = securityGetAuthorities().map { it.authority }
fun securityUserFirstName(): String {
    val details = securityPrincipal()
    return details.firstname
}
fun securityUserLastName(): String {
    val details = securityPrincipal()
    return details.lastname
}
fun securityUserName(): String {
    val details = securityPrincipal()
    return details.username
}
fun securityGetAuthenticationName(): String = SecurityContextHolder.getContext().authentication.name
fun securityGetPortalAccountId(): String = securityGetAuthenticationName()


