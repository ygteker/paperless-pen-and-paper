package lmu.msp.backend.utility

import org.springframework.security.core.Authentication

private const val WS_AUTH0_ID = "auth"

fun getAuth0IdFromAuthentication(authentication: Authentication): String {
    return authentication.name
}

fun getAuth0IdFromAttributes(attributes: MutableMap<String, Any>): String {
    return attributes[WS_AUTH0_ID].toString()
}

fun setAuth0IdToAttributes(attributes: MutableMap<String, Any>, auth0Id: String) {
    attributes[WS_AUTH0_ID] = auth0Id
}