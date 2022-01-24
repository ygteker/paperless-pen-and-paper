package lmu.msp.backend.configuration

import lmu.msp.backend.socket.CampaignHandler
import lmu.msp.backend.utility.setAuth0IdToAttributes
import lmu.msp.backend.utility.setCampaignIdToAttributes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.HandshakeInterceptor


@Configuration
@EnableWebSocket
class WebSocketConfig() : WebSocketConfigurer {

    /**
     * register websocket. every campaign uses another endpoint ("/1" "/2"...)
     *
     * @param registry
     */
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(campaignHandler(), "/ws/campaign/*").addInterceptors(
            auth0SessionInterceptor()
        )
    }

    @Bean
    fun campaignHandler(): WebSocketHandler {
        return CampaignHandler()
    }

    /**
     * used to write the campaigId and the auth0 id to the session attributes
     *
     * @return
     */
    @Bean
    fun auth0SessionInterceptor(): HandshakeInterceptor {
        return object : HandshakeInterceptor {
            override fun beforeHandshake(
                request: ServerHttpRequest,
                response: ServerHttpResponse,
                wsHandler: WebSocketHandler,
                attributes: MutableMap<String, Any>
            ): Boolean {
                //write auth0 id to session
                val authentication = SecurityContextHolder.getContext().authentication
                setAuth0IdToAttributes(attributes, authentication.name)

                //write campaign id to session
                val campaignId = getCampaignIdFromPath(request.uri.path) ?: return false
                setCampaignIdToAttributes(attributes, campaignId)

                return true //TODO disabled for easy testing campaignService.isMemberOrOwner(authentication.name, campaignId)
            }

            /**
             * tries to convert the last part of the path var to a long.
             *
             * @param path
             * @return campaignId or null if conversion fails
             */
            private fun getCampaignIdFromPath(path: String): Long? {
                val lastIndex = path.lastIndexOf('/')
                if (-1 == lastIndex) return null
                val campaignIdStr = path.substring(lastIndex + 1)

                return campaignIdStr.toLongOrNull()
            }

            override fun afterHandshake(
                request: ServerHttpRequest,
                response: ServerHttpResponse,
                wsHandler: WebSocketHandler,
                exception: Exception?
            ) {

                /*nothing to do*/
            }
        }
    }
}