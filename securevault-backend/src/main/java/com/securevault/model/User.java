// src/main/java/com/securevault/model/User.java
package com.securevault.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String passwordHash;

  // Store an auth token (UUID) for simple token-based auth
  @Column(length = 64)
  private String authToken;

  private Instant tokenCreatedAt;

  // role simple string, can be "user" or "admin"
  private String role = "user";

  // getters and setters (or use Lombok)

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public String getAuthToken() { return authToken; }
  public void setAuthToken(String authToken) { this.authToken = authToken; }
  public Instant getTokenCreatedAt() { return tokenCreatedAt; }
  public void setTokenCreatedAt(Instant tokenCreatedAt) { this.tokenCreatedAt = tokenCreatedAt; }
  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }
}
