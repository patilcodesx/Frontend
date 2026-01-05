package com.securevault.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long teamId;

    private Long  userId;
    private String email;
    private String role; // admin, uploader, viewer

    private String joinedAt;
}
