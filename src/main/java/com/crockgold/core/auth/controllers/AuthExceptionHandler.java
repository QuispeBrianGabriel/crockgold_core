package com.crockgold.core.auth.controllers;

import com.crockgold.core.auth.domain.errors.EmailAlreadyRegisteredError;
import com.crockgold.core.auth.domain.errors.InvalidCredentialsError;
import com.crockgold.core.auth.domain.errors.InvalidEmailError;
import com.crockgold.core.auth.domain.errors.InvalidPasswordError;
import com.crockgold.core.auth.domain.model.objects.Email;
import com.crockgold.core.auth.domain.model.objects.Password;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce los errores del módulo a {@link ProblemDetail}. Los ejemplos de {@link AuthApi} usan
 * estos mensajes.
 */
@RestControllerAdvice(assignableTypes = AuthController.class)
class AuthExceptionHandler {

  static final String EMAIL_ALREADY_REGISTERED = "Email already registered";
  static final String INVALID_CREDENTIALS = "Invalid email or password";
  static final String EMAIL_BLANK = "Email must not be blank";
  static final String EMAIL_TOO_LONG = "Email must not exceed " + Email.MAX_LENGTH + " characters";
  static final String EMAIL_NOT_EXACTLY_ONE_AT_SIGN = "Email must contain exactly one '@'";
  static final String EMAIL_LOCAL_TOO_LONG = "Email local part must not exceed "
    + Email.MAX_LOCAL_LENGTH + " characters";
  static final String EMAIL_INVALID_LOCAL_PART = "Email local part is not valid";
  static final String EMAIL_INVALID_DOMAIN = "Email domain is not valid";
  static final String PASSWORD_BLANK = "Password must not be blank";
  static final String PASSWORD_TOO_SHORT = "Password must have at least " + Password.MIN_LENGTH
    + " characters";
  static final String PASSWORD_INVALID_CHARACTERS = "Password must contain only unaccented letters, digits, symbols and spaces";
  static final String PASSWORD_TOO_LONG = "Password must not exceed " + Password.MAX_LENGTH
    + " characters";

  @ExceptionHandler
  ProblemDetail handle(EmailAlreadyRegisteredError exception) {
    return problem(HttpStatus.CONFLICT, EMAIL_ALREADY_REGISTERED);
  }

  @ExceptionHandler
  ProblemDetail handle(InvalidCredentialsError exception) {
    return problem(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS);
  }

  @ExceptionHandler
  ProblemDetail handle(InvalidEmailError exception) {
    String detail = switch (exception.reason()) {
      case BLANK -> EMAIL_BLANK;
      case TOO_LONG -> EMAIL_TOO_LONG;
      case NOT_EXACTLY_ONE_AT_SIGN -> EMAIL_NOT_EXACTLY_ONE_AT_SIGN;
      case LOCAL_TOO_LONG -> EMAIL_LOCAL_TOO_LONG;
      case INVALID_LOCAL_PART -> EMAIL_INVALID_LOCAL_PART;
      case INVALID_DOMAIN -> EMAIL_INVALID_DOMAIN;
    };
    return problem(HttpStatus.BAD_REQUEST, detail);
  }

  @ExceptionHandler
  ProblemDetail handle(InvalidPasswordError exception) {
    String detail = switch (exception.reason()) {
      case BLANK -> PASSWORD_BLANK;
      case TOO_SHORT -> PASSWORD_TOO_SHORT;
      case INVALID_CHARACTERS -> PASSWORD_INVALID_CHARACTERS;
      case TOO_LONG -> PASSWORD_TOO_LONG;
    };
    return problem(HttpStatus.BAD_REQUEST, detail);
  }

  private static ProblemDetail problem(HttpStatus status, String detail) {
    return ProblemDetail.forStatusAndDetail(status, detail);
  }
}
