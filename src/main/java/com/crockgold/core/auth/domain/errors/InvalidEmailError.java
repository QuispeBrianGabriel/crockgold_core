package com.crockgold.core.auth.domain.errors;

public class InvalidEmailError extends DomainError {

  public enum Reason {
    BLANK,
    TOO_LONG,
    NOT_EXACTLY_ONE_AT_SIGN,
    LOCAL_TOO_LONG,
    INVALID_LOCAL_PART,
    INVALID_DOMAIN
  }

  private final Reason reason;

  public InvalidEmailError(Reason reason) {
    super(reason.name());
    this.reason = reason;
  }

  public Reason reason() {
    return reason;
  }
}
