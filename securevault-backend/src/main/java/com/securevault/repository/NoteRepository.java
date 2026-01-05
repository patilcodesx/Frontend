// src/main/java/com/securevault/repository/NoteRepository.java
package com.securevault.repository;

import com.securevault.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
  List<Note> findByOwnerEmail(String ownerEmail);
}
