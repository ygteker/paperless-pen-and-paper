package lmu.msp.backend.utility

import org.springframework.security.core.Authentication

fun getAuth0IdFromAuthentication(authentication: Authentication): String {
    return authentication.name
}