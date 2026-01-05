package com.securevault.dto;

public record TeamCreateRequest(
        String name,
        String description,
        String encryptionKey
) {}
