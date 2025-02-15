package com.realtimecodeeditor.authenticationservice.controllers;

import com.realtimecodeeditor.authenticationservice.DTO.UserResponseDTO;
import com.realtimecodeeditor.authenticationservice.services.TokenBlacklistService;
import com.realtimecodeeditor.authenticationservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping(value="/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
            String username = jwtAuthToken.getName();
            UserResponseDTO user = userService.getCurrentUser(username);
            return ResponseEntity.ok(user);
        }
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

}
