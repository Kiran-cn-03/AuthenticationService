package com.realtimecodeeditor.authenticationservice.services;

import com.realtimecodeeditor.authenticationservice.DTO.UserResponseDTO;
import com.realtimecodeeditor.authenticationservice.models.User;
import com.realtimecodeeditor.authenticationservice.repository.UserRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getRole());
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getRole());
    }

}
