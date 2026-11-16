package com.crockgold.core.auth.repositories.entities;

import com.crockgold.core.auth.domain.model.User;
import com.crockgold.core.auth.domain.model.objects.Email;
import java.util.UUID;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Table("users")
public class UserEntity {

  @Id
  @With
  UUID id;

  Email email;
  String passwordHash;

  public static UserEntity from(User user) {
    UUID id = user.getId();
    Email email = user.getEmail();
    String passwordHash = user.getPasswordHash();
    return new UserEntity(id, email, passwordHash);
  }

  public User toModel() {
    return new User(id, email, passwordHash);
  }
}
