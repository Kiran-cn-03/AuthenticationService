package com.realtimecodeeditor.authenticationservice.models;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class SecureKeyGenerator {
    public static void main(String[] args) {
        String secretKey = Base64.getEncoder().encodeToString(
                Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded()
        );
        System.out.println("Generated Secret Key: " + secretKey);
    }
}