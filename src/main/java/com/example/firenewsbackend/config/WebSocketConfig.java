package com.example.firenewsbackend.config;
import com.example.firenewsbackend.manager.NotionWebSocketHandler;
import com.example.firenewsbackend.service.NotionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final NotionService notionService;

    public WebSocketConfig(NotionService notionService) {
        this.notionService = notionService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(notionWebSocketHandler(), "/ws")
                .setAllowedOriginPatterns("*");
    }

    @Bean
    public NotionWebSocketHandler notionWebSocketHandler() {
        return new NotionWebSocketHandler(notionService);
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}


//sk-b17db2716e2640e2b3cf7d829c4667bd deepSeek