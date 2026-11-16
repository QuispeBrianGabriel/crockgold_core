package com.crockgold.core.auth.errors;

public class InvalidPasswordError extends DomainError {

  public enum Reason {
    BLANK,
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
