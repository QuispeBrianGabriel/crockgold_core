package com.crockgold.core.auth.controllers;

import com.crockgold.core.auth.dto.LoginRequest;
import com.crockgold.core.auth.dto.RegisterRequest;
import com.crockgold.core.auth.dto.UserResponse;
import com.crockgold.core.auth.domain.model.objects.Email;
import com.crockgold.core.auth.domain.model.objects.Password;
import com.crockgold.core.auth.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  UserResponse register(@Valid @RequestBody RegisterRequest request) {
    Email email = new Email(request.email());
    Password password = new Password(request.password());
    return UserResponse.from(authService.register(email, password));
  }

  @PostMapping("/login")
  UserResponse login(@Valid @RequestBody LoginRequest request) {
    Email email = new Email(request.email());
    String password = request.password();
    return UserResponse.from(authService.login(email, password));
  }
}
