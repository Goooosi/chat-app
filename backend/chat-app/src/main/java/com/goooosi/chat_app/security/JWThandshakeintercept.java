package com.goooosi.chat_app.security;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
public class JWThandshakeintercept implements HandshakeInterceptor {

    private final JWTutil jwTutil;

    public JWThandshakeintercept(JWTutil jwTutil) {
        this.jwTutil = jwTutil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> properties) {

        List<String> cookieHeaders = request.getHeaders().get("Cookie");

        if (cookieHeaders != null) {
            for(String header: cookieHeaders) {
                String[] cookies = header.split(";");
                for(String cookie: cookies){
                    if(cookie.startsWith("jwt=")){
                        String token = cookie.substring(4);
                        if(jwTutil.validateJwtToken(token)){
                            String username = jwTutil.getUserFromToken(token);
                            properties.put("username", username);
                        }
                    }
                }
            }
        }

        return true;
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler webSocketHandler, Exception exception){}

}
