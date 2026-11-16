package com.crockgold.core.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
  @Schema(description = "Letras sin acentos, dígitos y los signos _ + - ' antes de la arroba,"
    + " con puntos sueltos entre ellos", example = "john@example.com", maxLength = 254) @NotBlank String email,
  @Schema(description = "Caracteres ASCII imprimibles: letras sin acentos, dígitos, símbolos y"
    + " espacios", example = "correct-horse-battery", minLength = 8, maxLength = 72, format = "password") @NotBlank String password) {
}
