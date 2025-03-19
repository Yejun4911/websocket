package com.dooribun.vreal.config

import com.dooribun.vreal.handler.SignalingHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebRtcConfig: WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
            registry.addHandler(signalingHandler(), "/ws")
                .setAllowedOrigins("*")
    }
    @Bean
    public fun signalingHandler(): SignalingHandler {
        return SignalingHandler()
    }
}