package com.goooosi.chat_app.model;

import java.time.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderUser;
    @Enumerated(EnumType.STRING)
    private TypeOfMessage type;
    private String receiverUser;
//    private Long receiverGroupId;
    @Column(columnDefinition = "TEXT")
    private String message;
    @CreationTimestamp
    private LocalDateTime timesent;
}
