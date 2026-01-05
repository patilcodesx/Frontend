// src/main/java/com/securevault/config/TokenInterceptor.java
package com.securevault.config;

import com.securevault.model.User;
import com.securevault.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Optional;

public class TokenInterceptor implements HandlerInterceptor {

  private final UserService userService;

  public TokenInterceptor(UserService userService) {
    this.userService = userService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
          throws Exception {

      // Always allow preflight
      if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
          return true;
      }

      String authHeader = request.getHeader("Authorization");

      // Allow /api/auth/** without token
      if (request.getRequestURI().startsWith("/api/auth")) {
          return true;
      }

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType("application/json");
          response.getWriter().write("{\"message\":\"Missing or invalid token\"}");
          return false;
      }

      String token = authHeader.substring(7);
      Optional<User> userOpt = userService.findByToken(token);

      if (userOpt.isEmpty()) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType("application/json");
          response.getWriter().write("{\"message\":\"Invalid or expired token\"}");
          return false;
      }

      request.setAttribute("currentUser", userOpt.get());
      return true;
  }

}
