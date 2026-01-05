package com.securevault.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Long  createdBy;
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "teamId", cascade = CascadeType.ALL)
    private List<TeamMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "teamId", cascade = CascadeType.ALL)
    private List<TeamActivity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "teamId", cascade = CascadeType.ALL)
    private List<TeamMessage> messages = new ArrayList<>();
}
