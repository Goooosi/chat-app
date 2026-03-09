package com.goooosi.chat_app.controller;

import com.goooosi.chat_app.model.TypeOfUser;
import com.goooosi.chat_app.model.User;
import com.goooosi.chat_app.repository.UserRepository;
import com.goooosi.chat_app.security.JWTutil;
import com.goooosi.chat_app.security.JwtRes;
import com.goooosi.chat_app.services.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody User user) {
        if(repository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new JwtRes("Username already exists."));
        }
        user.setUsername(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(TypeOfUser.USER);

        repository.save(user);

        String accessToken = jwTutil.generateJWTToken(user.getUsername());
        ResponseCookie accesscookie = ResponseCookie.from("jwt", accessToken)
                .httpOnly(true)
                .secure(false)// remember to true this
                .path("/")
                .maxAge(60*10)
                .sameSite("Lax")
                .build();
        String refreshToken = jwTutil.generateRefreshToken();
        ResponseCookie refreshcookie = ResponseCookie.from("refresh", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60*60*24)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accesscookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshcookie.toString())
                .body(new JwtRes("Logged in"));

    }

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwTutil.generateJWTToken(userDetails.getUsername());
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false) // change this later
                .path("/")
                .maxAge(86400)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new JwtRes("Logged in"));

    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Logged out");
    }
}
