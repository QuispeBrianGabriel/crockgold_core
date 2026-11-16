package com.crockgold.core.auth.domain.errors;

public class InvalidPasswordError extends DomainError {

  public enum Reason {
    BLANK,
    INVALID_CHARACTERS,
    TOO_SHORT,
    TOO_LONG
  }

  private final Reason reason;

  public InvalidPasswordError(Reason reason) {
    super(reason.name());
    this.reason = reason;
  }

  public Reason reason() {
    return reason;
  }
}
