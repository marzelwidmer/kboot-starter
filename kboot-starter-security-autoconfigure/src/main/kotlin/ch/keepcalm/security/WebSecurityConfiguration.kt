package ch.keepcalm.security

import org.springframework.boot.autoconfigure.security.SecurityProperties

abstract class WebSecurityConfiguration {

    companion object {
        @JvmStatic
        protected val INDEX_PATTERN = "/"

        @JvmStatic
        protected val DOCUMENTATION_PATTERN = "/api-docs/**"
    }

    protected fun getAdminRoles(securityProperties: SecurityProperties) = if (securityProperties.user.roles.isNotEmpty()) securityProperties.user.roles else listOf(ch.keepcalm.security.ROLE_ACTUATOR)
}
