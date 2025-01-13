package com.example.pfe;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
@Configuration
@EnableWebSocket
@CrossOrigin("*")
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(tutorialHandler(), "/websocket").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler tutorialHandler() {
        return new TutorialHandler();
    }

}