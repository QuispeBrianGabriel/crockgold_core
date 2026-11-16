package com.crockgold.core.auth.domain.errors;

import com.crockgold.core.auth.domain.model.objects.Email;

public class EmailAlreadyRegisteredError extends DomainError {

  private final transient Email email;

  public EmailAlreadyRegisteredError(Email email) {
    this.email = email;
  }

  public Email email() {
    return email;
  }
}
