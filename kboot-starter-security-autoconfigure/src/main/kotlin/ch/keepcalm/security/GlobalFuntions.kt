@file:JvmName("GlobalFunctions")
package ch.keepcalm.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

fun isPrincipalUser(): Boolean = getPrincipal() is CustomUserDetails
fun getPrincipal(): Any? = SecurityContextHolder.getContext().authentication?.principal
fun getPrincipalFirstName(): String {
    val details = getPrincipal() as CustomUserDetails
    return details.firstname
}
fun getPrincipalLastName(): String {
    val details = getPrincipal() as CustomUserDetails
    return details.lastname
}
fun getAuthenticationName(): String = SecurityContextHolder.getContext().authentication.name
fun getAuthorities(): Any? = SecurityContextHolder.getContext().authentication?.authorities
fun getCurrentUsername(): String = (SecurityContextHolder.getContext().authentication.principal as User).username
fun getBearerFromSecurityContext(): String = "Bearer ${SecurityContextHolder.getContext().authentication.credentials}"
