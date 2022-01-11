package lmu.msp.frontend

class Constants {
    companion object {
        const val API_BASE_PATH_DEMO = "https://msp-ws2122-5.mobile.ifi.lmu.de/"
        const val API_BASE_PATH = "https://msp-ws2122-5.mobile.ifi.lmu.de/api/v1/"
        //pen and paper api paths
        const val PATH_CAMPAIGN = "campaign"
        const val PATH_MEMBER = "$PATH_CAMPAIGN/member"
        const val PATH_USER = "user"
        const val PATH_HELLO_WORLD = "hello-world"
        const val PATH_HELLO_WORLD_AUTH = "$PATH_HELLO_WORLD/authentication"

        //paths for shared preferences
        const val SHARED_PATH_USER_TOKEN = "user_token"



    }
}