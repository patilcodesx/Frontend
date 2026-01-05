package com.securevault.service;

import com.securevault.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TokenService {

    // You can configure expiry time in application.properties:
    // securevault.token.expiry-ms=86400000
    @Value("${securevault.token.expiry-ms:0}")
    private long expiryMs;

    /**
     * Generate a new token for the user (UUID).
     */
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Assign new token & timestamp to user.
     */
    public void applyNewToken(User user) {
        user.setAuthToken(generateToken());
        user.setTokenCreatedAt(Instant.now());
    }

    /**
     * Validate token expiry (if expiry is enabled).
     * If expiryMs = 0, then token NEVER expires.
     */
    public boolean isTokenExpired(User user) {
        if (user.getTokenCreatedAt() == null) return true;
        if (expiryMs <= 0) return false; // expiry disabled
        Instant expiryTime = user.getTokenCreatedAt().plusMillis(expiryMs);
        return Instant.now().isAfter(expiryTime);
    }

    /**
     * Simple validation wrapper.
     */
    public boolean isValid(User user) {
        return user != null && user.getAuthToken() != null && !isTokenExpired(user);
    }
}
