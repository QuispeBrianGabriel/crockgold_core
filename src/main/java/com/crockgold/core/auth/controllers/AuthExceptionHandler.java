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

@RestControllerAdvice(assignableTypes = AuthController.class)
class AuthExceptionHandler {

  @ExceptionHandler
  ProblemDetail handle(EmailAlreadyRegisteredError exception) {
    return problem(HttpStatus.CONFLICT, "Email already registered");
  }

  @ExceptionHandler
  ProblemDetail handle(InvalidCredentialsError exception) {
    return problem(HttpStatus.UNAUTHORIZED, "Invalid email or password");
  }

  @ExceptionHandler
  ProblemDetail handle(InvalidEmailError exception) {
    String detail = switch (exception.reason()) {
      case BLANK -> "Email must not be blank";
      case TOO_LONG -> "Email must not exceed " + Email.MAX_LENGTH + " characters";
      case NOT_EXACTLY_ONE_AT_SIGN -> "Email must contain exactly one '@'";
      case LOCAL_TOO_LONG -> "Email local part must not exceed " + Email.MAX_LOCAL_LENGTH
        + " characters";
      case INVALID_LOCAL_PART -> "Email local part is not valid";
      case INVALID_DOMAIN -> "Email domain is not valid";
    };
    return problem(HttpStatus.BAD_REQUEST, detail);
  }

  @ExceptionHandler
  ProblemDetail handle(InvalidPasswordError exception) {
    String detail = switch (exception.reason()) {
      case BLANK -> "Password must not be blank";
      case TOO_SHORT -> "Password must have at least " + Password.MIN_LENGTH + " characters";
      case INVALID_CHARACTERS ->
        "Password must contain only unaccented letters, digits, symbols and spaces";
      case TOO_LONG -> "Password must not exceed " + Password.MAX_LENGTH + " characters";
    };
    return problem(HttpStatus.BAD_REQUEST, detail);
  }

  private static ProblemDetail problem(HttpStatus status, String detail) {
    return ProblemDetail.forStatusAndDetail(status, detail);
  }
}
