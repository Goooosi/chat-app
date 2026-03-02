package com.goooosi.chat_app.model;

import jakarta.persistence.*;

import java.time.Instant;

import lombok.*;

import com.goooosi.chat_app.model.User;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @Column(nullable = false, length = 512)
    private String token;
    @Column(nullable = false)
    private Instant expiresAt;
    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
