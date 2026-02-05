package com.goooosi.chat_app.components;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WebSocketListener {

    private final Map<String, String> onlineusers = new ConcurrentHashMap<>();

    @EventListener
    public void connected(SessionConnectedEvent event){
        Principal user = StompHeaderAccessor.wrap(event.getMessage()).getUser();
        if(user != null){
            onlineusers.put(event.getMessage().getHeaders().get("simpSessionId").toString(), user.getName());
        }
    }

    @EventListener
    public void disconnected(SessionDisconnectEvent event){
        Principal user = event.getUser();
        if(user != null){
            onlineusers.remove(event.getSessionId());
        }
    }

    public Set<String> getOnlineUsers(){
        return new HashSet<>(onlineusers.values());
    }
}