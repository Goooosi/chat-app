package com.goooosi.chat_app.security;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JWThandshakeintercept implements HandshakeInterceptor {

    private final JWTutil jwTutil;

    public JWThandshakeintercept(JWTutil jwTutil) {
        this.jwTutil = jwTutil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler webSocketHandler, Map<String, Object> properties) {
        if (request instanceof ServletServerHttpRequest serverHttpRequest ) {
            String authHeader = serverHttpRequest.getServletRequest().getHeader("Authorization");
            if ((authHeader != null) && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if(jwTutil.validateJwtToken(token)){
                    String username = jwTutil.getUserFromToken(token);
                    properties.put("username", username);
                }
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler webSocketHandler, Exception exception){}

}
