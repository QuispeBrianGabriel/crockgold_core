package com.crockgold.core.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
  @NotBlank String email, @NotBlank String password) {
}
