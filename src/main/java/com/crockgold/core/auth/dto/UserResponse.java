package com.crockgold.core.auth.dto;

import com.crockgold.core.auth.domain.model.User;
import java.util.UUID;

public record UserResponse(UUID id, String email) {

  public static UserResponse from(User user) {
    return new UserResponse(user.getId(), user.getEmail().value());
  }
}
