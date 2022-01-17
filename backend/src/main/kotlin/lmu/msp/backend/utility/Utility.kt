package lmu.msp.backend.utility

import org.springframework.security.core.Authentication

private const val WS_AUTH0_ID = "auth"
private const val WS_CAMPAIGN_ID = "campaignId"

fun getAuth0IdFromAuthentication(authentication: Authentication): String {
    return authentication.name
}

fun getAuth0IdFromAttributes(attributes: MutableMap<String, Any>): String {
    return attributes[WS_AUTH0_ID].toString()
}

fun setAuth0IdToAttributes(attributes: MutableMap<String, Any>, auth0Id: String) {
    attributes[WS_AUTH0_ID] = auth0Id
}

fun getCampaignIdFromAttributes(attributes: MutableMap<String, Any>): Long {
    return attributes[WS_CAMPAIGN_ID] as Long
}

fun setCampaignIdToAttributes(attributes: MutableMap<String, Any>, campaignId: Long) {
    attributes[WS_CAMPAIGN_ID] = campaignId
}