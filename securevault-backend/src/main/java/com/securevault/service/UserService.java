// src/main/java/com/securevault/service/UserService.java
package com.securevault.service;

import com.securevault.model.User;
import com.securevault.repository.UserRepository;
import com.securevault.util.PasswordUtil;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User register(String name, String email, String password) {
    User user = new User();
    user.setName(name);
    user.setEmail(email.toLowerCase());
    user.setPasswordHash(PasswordUtil.hash(password));
    return userRepository.save(user);
  }

  public Optional<User> login(String email, String password) {
    Optional<User> u = userRepository.findByEmail(email.toLowerCase());
    if (u.isPresent()) {
      User user = u.get();
      if (PasswordUtil.matches(password, user.getPasswordHash())) {
        String token = UUID.randomUUID().toString();
        user.setAuthToken(token);
        user.setTokenCreatedAt(Instant.now());
        userRepository.save(user);
        return Optional.of(user);
      }
    }
    return Optional.empty();
  }

  public Optional<User> findByToken(String token) {
    if (token == null) return Optional.empty();
    return userRepository.findByAuthToken(token);
  }
  public List<User> findAllUsers() {
	    return userRepository.findAll();
	}


  public void logout(User user) {
    user.setAuthToken(null);
    user.setTokenCreatedAt(null);
    userRepository.save(user);
  }
}
