package com.example.firenewsbackend.config;

import com.example.firenewsbackend.ws.YourWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
// sk-b17db2716e2640e2b3cf7d829c4667bd deepSeek

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new YourWebSocketHandler(), "/websocket/endpoint")
                .setAllowedOrigins("*"); // 根据需要设置允许的来源
    }

}

