package com.goooosi.chat_app.components;


import com.goooosi.chat_app.util.StompPrincipals;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
@Component
public class HandShakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest serverHttpRequest, WebSocketHandler webSocketHandler, Map<String, Object> properties) {
        String username = (String) properties.get("username");
        if(username != null) {
            return new StompPrincipals(username);
        }
        return null;
    }
}
