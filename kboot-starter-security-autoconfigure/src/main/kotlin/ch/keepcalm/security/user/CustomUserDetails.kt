package ch.keepcalm.security.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUserDetails(
    subject: String?,
    password: String?,
    authorities: List<GrantedAuthority>,
    val firstname: String,
    val lastname: String,
    val language: String = "de"
) : User(subject, password, authorities)
