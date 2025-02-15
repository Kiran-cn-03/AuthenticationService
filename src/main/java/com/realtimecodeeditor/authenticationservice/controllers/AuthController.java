package com.realtimecodeeditor.authenticationservice.controllers;

import com.realtimecodeeditor.authenticationservice.DTO.LoginRequestDto;
import com.realtimecodeeditor.authenticationservice.DTO.LoginResponseDto;
import com.realtimecodeeditor.authenticationservice.models.User;
import com.realtimecodeeditor.authenticationservice.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
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
}
