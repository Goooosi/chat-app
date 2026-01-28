package com.goooosi.chat_app.services;

import com.goooosi.chat_app.model.Message;
import com.goooosi.chat_app.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MessageService {

    private final MessageRepository repository;

    public MessageService(MessageRepository repository){
        this.repository = repository;
    }

    public Message save(Message message){
        return repository.save(message);
    }

    public List<Message> getMessagesByGroup(Long id){
        return repository.findByReceiverGroupId(id);
    }

    public List<Message> getMessagesByBetweenUser(String sender, String receiver){
        return repository.findPrivateChat(sender, receiver);
    }

}
