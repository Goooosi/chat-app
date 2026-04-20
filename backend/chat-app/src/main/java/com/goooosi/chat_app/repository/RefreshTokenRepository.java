package com.goooosi.chat_app.repository;

import com.goooosi.chat_app.model.RefreshTokens;
import com.goooosi.chat_app.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository  extends JpaRepository<RefreshTokens, Long> {
    Optional<RefreshTokens> findByToken(String token);
    @Transactional
    void deleteByToken(String token);

}
