package lmu.msp.frontend

class Constants {
    companion object {
        const val API_BASE_PATH = "https://msp-ws2122-5.mobile.ifi.lmu.de/api/v1/"

        //pen and paper api paths
        const val PATH_CAMPAIGN = "campaign"
        const val PATH_MEMBER = "$PATH_CAMPAIGN/member"
        const val PATH_MEMBER_INVITE_LINK = "$PATH_MEMBER/invite/link"
        const val PATH_MEMBER_INVITE_ACCEPT = "$PATH_MEMBER/invite/accept"
        const val PATH_USER = "user"
    }
}