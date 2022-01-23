package lmu.msp.backend.utility

import org.springframework.security.core.Authentication

private const val WS_AUTH0_ID = "auth"
private const val WS_CAMPAIGN_ID = "campaignId"

/**
 * get an auth0 id
 * the authentication name will be handled as the auth0 id
 *
 * @param authentication
 * @return an auth0 id from the provided auth obj
 */
fun getAuth0IdFromAuthentication(authentication: Authentication): String {
    return authentication.name
}

/**
 * get an auth0 id from an attributes map (provided in every websocket session)
 *
 * @param attributes
 * @return auth0 id
 */
fun getAuth0IdFromAttributes(attributes: MutableMap<String, Any>): String {
    return attributes[WS_AUTH0_ID].toString()
}

/**
 * write the auth0 id to the attributes
 *
 * @param attributes
 * @param auth0Id
 */
fun setAuth0IdToAttributes(attributes: MutableMap<String, Any>, auth0Id: String) {
    attributes[WS_AUTH0_ID] = auth0Id
}

/**
 * get the campaignId from the attributes
 * campaignId must have been written beforehand
 *
 * @param attributes
 * @return campaignId
 */
fun getCampaignIdFromAttributes(attributes: MutableMap<String, Any>): Long {
    return attributes[WS_CAMPAIGN_ID] as Long
}

/**
 * write the campaignId to the attributes
 *
 * @param attributes
 * @param campaignId
 */
fun setCampaignIdToAttributes(attributes: MutableMap<String, Any>, campaignId: Long) {
    attributes[WS_CAMPAIGN_ID] = campaignId
}