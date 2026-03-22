package com.goooosi.chat_app.controller;

import com.goooosi.chat_app.dto.UserDTO;
import com.goooosi.chat_app.model.RefreshTokens;
import com.goooosi.chat_app.model.TypeOfUser;
import com.goooosi.chat_app.model.User;
import com.goooosi.chat_app.repository.RefreshTokenRepository;
import com.goooosi.chat_app.repository.UserRepository;
import com.goooosi.chat_app.security.JWTutil;
import com.goooosi.chat_app.security.JwtRes;
import com.goooosi.chat_app.services.UserService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
        RefreshTokenRepository refreshRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody UserDTO dto) {
        if(repository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new JwtRes("Username already exists."));
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(TypeOfUser.USER);

        repository.save(user);

        String accessToken = jwTutil.generateJWTToken(dto.getUsername());
        ResponseCookie accesscookie = ResponseCookie.from("jwt", accessToken)
                .httpOnly(true)
                .secure(false)// remember to true this
                .path("/")
                .maxAge(60*10)
                .sameSite("Lax")
                .build();
        String refreshToken = jwTutil.generateRefreshToken();
        RefreshTokens tokens = new RefreshTokens();
        tokens.setUsername(dto.getUsername());
        tokens.setToken(refreshToken);
        Instant time = Instant.now().plus(24, ChronoUnit.HOURS);
        tokens.setExpiresAt(time);
        refreshRepository.save(tokens);
        ResponseCookie refreshcookie = ResponseCookie.from("refresh", refreshToken)
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
    public ResponseEntity<?> auth(@RequestBody UserDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
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
        String refreshToken = jwTutil.generateRefreshToken();
        RefreshTokens tokens = new RefreshTokens();
        tokens.setToken(refreshToken);
        tokens.setUsername(userDetails.getUsername());
        Instant time = Instant.now().plus(24, ChronoUnit.HOURS);
        tokens.setExpiresAt(time);
        refreshRepository.save(tokens);
        ResponseCookie refreshcookie = ResponseCookie.from("refresh", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60*60*24)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshcookie.toString())
                .body(new JwtRes("Logged in"));

    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest httpServletRequest){
        if(httpServletRequest.getCookies() != null){
            for(Cookie cookie: httpServletRequest.getCookies()){
                if(cookie.getName().equals("refresh")){
                    String refcookie = cookie.getValue();
                    if(refreshRepository.findByToken(refcookie).isPresent()){
                        RefreshTokens token = refreshRepository.findByToken(refcookie).orElseThrow();
                        if(token.getExpiresAt().isAfter(Instant.now())){
                            String user = token.getUsername();
                            String AccessToken = jwTutil.generateJWTToken(user);
                            ResponseCookie AccessCookie = ResponseCookie.from("jwt", AccessToken)
                                    .httpOnly(true)
                                    .secure(false)// remember to true this
                                    .path("/")
                                    .maxAge(60*10)
                                    .sameSite("Lax")
                                    .build();
                            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, AccessCookie.toString())
                                    .body(new JwtRes("Token Refreshed"));
                        } else {
                            refreshRepository.deleteByToken(refcookie);
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtRes("Re-login"));
                        }
                    }
                }
            }
        }
        return ResponseEntity.badRequest().body(new JwtRes("Error"));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getCookies() != null) {
            for(Cookie cookie: httpServletRequest.getCookies()) {
                if(cookie.getName().equals("refresh")) {
                    String refcookie = cookie.getValue();
                    refreshRepository.deleteByToken(refcookie);
                }
            }
        }
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        ResponseCookie refresh = ResponseCookie.from("refresh", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.SET_COOKIE, refresh.toString())
                .body(new JwtRes("Logged In"));
    }
}
