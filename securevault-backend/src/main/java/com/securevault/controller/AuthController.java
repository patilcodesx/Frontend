// src/main/java/com/securevault/controller/AuthController.java
package com.securevault.controller;

import com.securevault.dto.*;
import com.securevault.model.User;
import com.securevault.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
    if (req.getEmail() == null || req.getPassword() == null || req.getName() == null)
      return ResponseEntity.badRequest().body(new ApiResponse(false, "Missing fields"));

    if (userService.findByEmail(req.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().body(new ApiResponse(false, "Email already registered"));
    }
    User created = userService.register(req.getName(), req.getEmail(), req.getPassword());
    return ResponseEntity.ok(new ApiResponse(true, "User registered"));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest req) {
    Optional<User> o = userService.login(req.getEmail(), req.getPassword());
    if (o.isEmpty()) {
      return ResponseEntity.status(401).body(new ApiResponse(false, "Invalid credentials"));
    }
    User user = o.get();
    return ResponseEntity.ok(new AuthResponse(user.getAuthToken(), user.getEmail(), user.getName()));
  }
  
  @GetMapping("/users")
  public ResponseEntity<?> getAllUsers() {
      return ResponseEntity.ok(userService.findAllUsers());
  }


  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestHeader(name = "Authorization", required = false) String authHeader, @RequestAttribute(name = "currentUser", required = false) User currentUser) {
    if (currentUser == null) {
      // Try to logout by token if token in header
      return ResponseEntity.status(401).body(new ApiResponse(false, "Not logged in"));
    }
    userService.logout(currentUser);
    return ResponseEntity.ok(new ApiResponse(true, "Logged out"));
  }
}
