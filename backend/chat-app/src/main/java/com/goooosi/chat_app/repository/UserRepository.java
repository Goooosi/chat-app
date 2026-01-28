package com.goooosi.chat_app.repository;

import com.goooosi.chat_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u.username FROM User u WHERE u.id= :id")
    Optional<String> getUsernameFromId(@Param("id") Long id);
}
