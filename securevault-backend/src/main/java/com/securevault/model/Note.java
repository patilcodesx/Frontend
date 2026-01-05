// src/main/java/com/securevault/model/Note.java
package com.securevault.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notes")
public class Note {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String ownerEmail;

  @Column(columnDefinition = "TEXT")
  private String contentEncrypted; // store encrypted note content

  private Instant createdAt;

  // getters/setters
  // ...
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getOwnerEmail() { return ownerEmail; }
  public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
  public String getContentEncrypted() { return contentEncrypted; }
  public void setContentEncrypted(String contentEncrypted) { this.contentEncrypted = contentEncrypted; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
