@file:JvmName("GlobalFunctions")
package ch.keepcalm.security

import ch.keepcalm.security.user.CustomUserDetails
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User


const val AUTHENTICATION_SCHEMA = "Bearer "
const val ROLE_KEEPCALM_ADMIN = "keepcalm.admin"
const val ROLE_KEEPCALM_USER = "keepcalm.user"
const val ROLE_ACTUATOR = "ACTUATOR"


fun securityPrincipal(): Any? = SecurityContextHolder.getContext().authentication?.principal
fun securityCurrentUserBearerToken(): String = "Bearer ${SecurityContextHolder.getContext().authentication.credentials}"
fun securityGetAuthorities(): MutableCollection<out GrantedAuthority>? = SecurityContextHolder.getContext().authentication?.authorities
fun securityIsCurrentUserInRoleAdmin(): Boolean = securityGetRoles().contains(ROLE_KEEPCALM_ADMIN)
fun securityIsCurrentUserInRoleUser(): Boolean = securityGetRoles().contains(ROLE_KEEPCALM_USER)
fun securityGetRoles(): List<String> = securityGetAuthorities()?.map { it.authority } ?: emptyList()
fun securityUserFirstName(): String {
    val details = securityPrincipal() as CustomUserDetails
    return details.firstname
}
fun securityUserFirstLastName(): String {
    val details = securityPrincipal() as CustomUserDetails
    return details.lastname
}


fun securityUserName(): String = (SecurityContextHolder.getContext().authentication.principal as User).username

fun securityGetAuthenticationName(): String = SecurityContextHolder.getContext().authentication.name
fun securityCurrentUserName(): String = (SecurityContextHolder.getContext().authentication.principal as User).username


