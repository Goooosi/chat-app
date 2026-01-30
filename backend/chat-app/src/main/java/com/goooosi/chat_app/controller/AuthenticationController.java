package com.goooosi.chat_app.controller;

import com.goooosi.chat_app.model.User;
import com.goooosi.chat_app.repository.UserRepository;
import com.goooosi.chat_app.security.GeneralRes;
import com.goooosi.chat_app.security.JWTutil;
import com.goooosi.chat_app.security.JwtRes;
import com.goooosi.chat_app.services.UserService;
import com.goooosi.chat_app.util.JwtReq;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
        @Autowired
        AuthenticationManager authenticationManager;
        @Autowired
        UserRepository repository;
        @Autowired
        PasswordEncoder encoder;
        @Autowired
        JWTutil jwTutil;
        @Autowired
        GeneralRes generalRes;



    @PostMapping("/signup")
    public ResponseEntity<JwtRes> register(@RequestBody User user) {
        if(repository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new JwtRes("Username already exists."));
        }
        user.setUsername(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setDatejoined(LocalDate.now());
        user.setRole("ROLE_USER");

        repository.save(user);

        String token = jwTutil.generateJWTToken(user.getUsername());

        return ResponseEntity.ok(new JwtRes(token));

    }

    @PostMapping("/login")
    public ResponseEntity<JwtRes> auth(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwTutil.generateJWTToken(userDetails.getUsername());
        return ResponseEntity.ok(new JwtRes(token));

    }
    @PostMapping("/validate")
    public boolean validate(@RequestHeader JwtReq token) {
        return jwTutil.validateJwtToken(token.getToken().substring(7));
    }

    @PostMapping("/convert")
    public String convert(@RequestHeader JwtReq token) {
        return jwTutil.getUserFromToken(token.getToken().substring(7));
    }
}
