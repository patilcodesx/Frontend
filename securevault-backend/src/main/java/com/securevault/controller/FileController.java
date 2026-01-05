package com.securevault.controller;

import com.securevault.dto.ApiResponse;
import com.securevault.model.FileMeta;
import com.securevault.model.User;
import com.securevault.repository.FileRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    private final FileRepository fileRepository;

    public FileController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    // -----------------------------------------------------------
    // GET: All files of logged-in user
    // -----------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<FileMeta>> listFiles(HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        List<FileMeta> files = fileRepository.findByOwnerEmail(user.getEmail());
        return ResponseEntity.ok(files);
    }

    // -----------------------------------------------------------
    // POST: Upload encrypted file (from frontend)
    // -----------------------------------------------------------
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody FileMeta meta, HttpServletRequest request) {

        User user = (User) request.getAttribute("currentUser");

        // 🔥 Ensure encryptionKey exists (required for decrypt)
        if (meta.getEncryptionKey() == null || meta.getEncryptionKey().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Missing encryptionKey"));
        }

        if (meta.getEncryptedData() == null || meta.getIv() == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Missing encryptedData or iv"));
        }

        meta.setOwnerEmail(user.getEmail());
        meta.setUploadedAt(Instant.now());

        FileMeta saved = fileRepository.save(meta);

        return ResponseEntity.ok(new ApiResponse(true, "File uploaded", saved));
    }

    // -----------------------------------------------------------
    // DELETE file
    // -----------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");

        return fileRepository.findById(id)
                .map(f -> {
                    if (!f.getOwnerEmail().equals(user.getEmail())) {
                        return ResponseEntity.status(403)
                                .body(new ApiResponse(false, "Forbidden"));
                    }
                    fileRepository.delete(f);
                    return ResponseEntity.ok(new ApiResponse(true, "Deleted"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
