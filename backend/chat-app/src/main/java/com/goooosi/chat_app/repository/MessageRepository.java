package com.goooosi.chat_app.repository;

import com.goooosi.chat_app.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverGroupId(Long groupId);

    @Query("SELECT m FROM Message m WHERE (m.senderUser = :sender AND m.receiverUser = :receiver) OR (m.senderUser = :receiver AND m.receiverUser = :sender)")
    List<Message> findPrivateChat(String sender, String receiver);

}
