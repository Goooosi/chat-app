package com.goooosi.chat_app.controller;

import com.goooosi.chat_app.model.Message;
import com.goooosi.chat_app.services.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/private")
    public List<Message> getPrivateMessages(@RequestParam String sender, @RequestParam String receiver) {
        return messageService.getMessagesByBetweenUser(sender, receiver);
    }
}
