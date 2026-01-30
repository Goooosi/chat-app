package com.goooosi.chat_app.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTutil {
    @Value("${jwt.secret}")
    private String jwtsecret;
    @Value("${jwt.expiration}")
    private int jwtExpiration;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtsecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateJWTToken(String Username) {
        return Jwts.builder()
                .subject(Username)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime()+jwtExpiration)))
                .signWith(key)
                .compact();
    }

    public String getUserFromToken(String token){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

