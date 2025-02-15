package com.realtimecodeeditor.authenticationservice.controllers;

import com.realtimecodeeditor.authenticationservice.DTO.LoginRequestDto;
import com.realtimecodeeditor.authenticationservice.DTO.LoginResponseDto;
import com.realtimecodeeditor.authenticationservice.models.User;
import com.realtimecodeeditor.authenticationservice.services.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.realtimecodeeditor.authenticationservice.services.TokenBlacklistService;
import java.util.concurrent.TimeUnit;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;
    public AuthController(AuthService authService, TokenBlacklistService tokenBlacklistService){
        this.authService = authService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        return ResponseEntity.ok(authService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto){
        String token = authService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid token format");
        }

        String jwt = token.substring(7); // Remove "Bearer "
        long expiryTime = getTokenExpiry(jwt);

        if (expiryTime > 0) {
            redisTemplate.opsForValue().set(jwt, "blacklisted", expiryTime, TimeUnit.MILLISECONDS);
            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.badRequest().body("Token is already expired");
        }
    }
    private long getTokenExpiry(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor("WXM+f43YRjGPMPByBsksp7v/OwyEHgX4N9W8ze7kXes=".getBytes(StandardCharsets.UTF_8)))
                    .build();

            Claims claims = parser.parseSignedClaims(token).getPayload();

            Date expiration = claims.getExpiration();
            long expiryTime = expiration.getTime() - System.currentTimeMillis();
            System.out.println(expiryTime);
            // Ensure expiryTime is not negative (already expired token)
            return expiryTime > 0 ? expiryTime : 1; // At least 1 millisecond to store in Redis
        } catch (Exception e) {
            System.out.println("Error parsing token: " + e.getMessage());
            return -1; // Indicate an error in parsing (expired or invalid token)
        }
    }
}
