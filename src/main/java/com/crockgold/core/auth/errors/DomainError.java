package com.crockgold.core.auth.errors;

public abstract class DomainError extends RuntimeException {

  private static final long serialVersionUID = 1L;

  protected DomainError() {
    super();
  }

  protected DomainError(String message) {
    super(message);
  }
}
