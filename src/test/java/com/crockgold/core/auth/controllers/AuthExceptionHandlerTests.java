package com.crockgold.core.auth.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.crockgold.core.auth.errors.InvalidEmailError;
import com.crockgold.core.auth.errors.InvalidPasswordError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

/**
 * Los datos en blanco los detiene antes la validación de la petición, así que estas respuestas no
 * se alcanzan desde la API y se prueban aquí.
 */
class AuthExceptionHandlerTests {

  private final AuthExceptionHandler handler = new AuthExceptionHandler();

  @Test
  @DisplayName("Un email en blanco se responde como petición incorrecta")
  void blankEmailIsBadRequest() {
    var error = new InvalidEmailError(InvalidEmailError.Reason.BLANK);

    ProblemDetail problem = handler.handle(error);

    assertThat(problem.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(problem.getDetail()).isEqualTo("Email must not be blank");
  }

  @Test
  @DisplayName("Una contraseña en blanco se responde como petición incorrecta")
  void blankPasswordIsBadRequest() {
    var error = new InvalidPasswordError(InvalidPasswordError.Reason.BLANK);

    ProblemDetail problem = handler.handle(error);

    assertThat(problem.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(problem.getDetail()).isEqualTo("Password must not be blank");
  }
}
