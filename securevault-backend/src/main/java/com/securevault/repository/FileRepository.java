// src/main/java/com/securevault/repository/FileRepository.java
package com.securevault.repository;

import com.securevault.model.FileMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FileRepository extends JpaRepository<FileMeta, Long> {
  List<FileMeta> findByOwnerEmail(String ownerEmail);
}
