// src/main/java/com/securevault/controller/NoteController.java
package com.securevault.controller;

import com.securevault.model.Note;
import com.securevault.model.User;
import com.securevault.repository.NoteRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

  private final NoteRepository noteRepository;

  public NoteController(NoteRepository noteRepository) { this.noteRepository = noteRepository; }

  @GetMapping
  public ResponseEntity<List<Note>> listNotes(HttpServletRequest request) {
    User user = (User) request.getAttribute("currentUser");
    return ResponseEntity.ok(noteRepository.findByOwnerEmail(user.getEmail()));
  }

  @PostMapping
  public ResponseEntity<Note> createNote(@RequestBody Note note, HttpServletRequest request) {
    User user = (User) request.getAttribute("currentUser");
    note.setOwnerEmail(user.getEmail());
    note.setCreatedAt(Instant.now());
    return ResponseEntity.ok(noteRepository.save(note));
  }
}
