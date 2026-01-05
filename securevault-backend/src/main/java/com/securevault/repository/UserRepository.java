// src/main/java/com/securevault/repository/UserRepository.java
package com.securevault.repository;

import com.securevault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  Optional<User> findByAuthToken(String authToken);
}
