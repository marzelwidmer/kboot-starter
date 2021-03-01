package ch.keepcalm.security

import org.springframework.stereotype.Service

@Service
class SecurityService {
    fun getPrincipal() = ch.keepcalm.security.securityPrincipal()
    fun isPrincipalUser() = ch.keepcalm.security.securityPrincipal()
    fun getBearerFromSecurityContext() = ch.keepcalm.security.securityCurrentUserBearerToken()
    fun getAuthorities() = ch.keepcalm.security.securityGetAuthorities()
    fun isCurrentUserInRoleAdmin() = ch.keepcalm.security.securityIsCurrentUserInRoleAdmin()
    fun isCurrentUserInRoleUser() = ch.keepcalm.security.securityIsCurrentUserInRoleUser()
    fun getRoles() = ch.keepcalm.security.securityGetRoles()
    fun getPrincipalFirstName() =ch.keepcalm.security.securityUserFirstName()
    fun getPrincipalLastName() = ch.keepcalm.security.securityUserFirstLastName()
    fun getAuthenticationName() = ch.keepcalm.security.securityGetAuthenticationName()
    fun getCurrentUsername() = ch.keepcalm.security.securityCurrentUserName()
}
