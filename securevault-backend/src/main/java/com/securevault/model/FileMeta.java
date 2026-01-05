package com.securevault.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
public class FileMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerEmail;

    private String name;
    private long size;
    private String type;

    @Lob
    private String encryptedData;

    private String iv;

    // ✅ ADD THIS FIELD
    @Lob
    private String encryptionKey;

    private String teamId;

    private Instant uploadedAt;
}
