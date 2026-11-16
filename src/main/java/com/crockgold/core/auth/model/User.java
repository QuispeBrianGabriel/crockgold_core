package com.crockgold.core.auth.model;

import com.crockgold.core.auth.model.objects.Email;
import java.util.UUID;
import lombok.Value;

@Value
public class User {

  UUID id;
  Email email;
  String passwordHash;

  public static User create(Email email, String passwordHash) {
    return new User(null, email, passwordHash);
  }
}
