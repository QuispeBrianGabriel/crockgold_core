package com.crockgold.core.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
  @Schema(description = "Se compara sin espacios laterales y sin distinguir mayúsculas", example = "john@example.com", maxLength = 254) @NotBlank String email,
  @Schema(description = "Se compara tal cual, con espacios y mayúsculas", example = "correct-horse-battery", format = "password") @NotBlank String password) {
}
