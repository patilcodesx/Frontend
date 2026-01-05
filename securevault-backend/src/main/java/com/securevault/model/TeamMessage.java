package com.securevault.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TeamMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long teamId;
    private String userId;

    @Lob
    private String encryptedContent;

    private String iv;
    private String timestamp;
}
