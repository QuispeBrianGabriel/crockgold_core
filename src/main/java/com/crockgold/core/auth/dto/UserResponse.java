package com.crockgold.core.auth.dto;

import com.crockgold.core.auth.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record UserResponse(
  @Schema(example = "019a8f3c-2b7d-7e4a-9c1f-5d6e7f8a9b0c") UUID id,
  @Schema(description = "Normalizado: sin espacios laterales y en minúsculas", example = "john@example.com") String email) {

  public static UserResponse from(User user) {
    return new UserResponse(user.getId(), user.getEmail().value());
  }
}
