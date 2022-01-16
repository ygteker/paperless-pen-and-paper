package lmu.msp.backend.configuration

import lmu.msp.backend.socket.MyHandler
import lmu.msp.backend.utility.setAuth0IdToAttributes
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

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(myHandler(), "/myHandler").addInterceptors(
            auth0SessionInterceptor()
        )
    }

    @Bean
    fun myHandler(): WebSocketHandler {
        return MyHandler()
    }

    @Bean
    fun auth0SessionInterceptor(): HandshakeInterceptor {
        return object : HandshakeInterceptor {
            override fun beforeHandshake(
                request: ServerHttpRequest,
                response: ServerHttpResponse,
                wsHandler: WebSocketHandler,
                attributes: MutableMap<String, Any>
            ): Boolean {
                val authentication = SecurityContextHolder.getContext().authentication
                setAuth0IdToAttributes(attributes, authentication.name)
                return true
            }

            override fun afterHandshake(
                request: ServerHttpRequest,
                response: ServerHttpResponse,
                wsHandler: WebSocketHandler,
                exception: Exception?
            ) {
            }
        }
    }
}