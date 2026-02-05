package com.goooosi.chat_app.controller;

import com.goooosi.chat_app.model.Message;
import com.goooosi.chat_app.services.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/group")
    public Message receiveGroupMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSend("/group/" + message.getReceiverGroupId(), message);
        return message;

    }

    @MessageMapping("/private-message")
    public Message receivePrivateMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverUser(), "/queue/private", message);
        return message;
    }
}
