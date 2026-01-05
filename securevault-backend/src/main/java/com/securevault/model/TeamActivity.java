package com.securevault.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TeamActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long teamId;
    private String userId;
    private String action;
    private String details;
    private String timestamp;
}
