package com.goooosi.chat_app.config;


import com.goooosi.chat_app.components.HandShakeHandler;
import com.goooosi.chat_app.security.JWThandshakeintercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private final HandShakeHandler handShakeHandler;
    private final JWThandshakeintercept jwThandshakeintercept;

    public WebSocketConfig(HandShakeHandler handShakeHandler, JWThandshakeintercept jwThandshakeintercept) {
        this.handShakeHandler = handShakeHandler;
        this.jwThandshakeintercept = jwThandshakeintercept;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/group", "/user");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOriginPatterns("*").addInterceptors(jwThandshakeintercept).setHandshakeHandler(handShakeHandler);
    }
}
