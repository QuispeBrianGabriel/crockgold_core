package com.crockgold.core.auth.controllers;

import static com.crockgold.core.auth.controllers.AuthExceptionHandler.EMAIL_ALREADY_REGISTERED;
import static com.crockgold.core.auth.controllers.AuthExceptionHandler.EMAIL_INVALID_DOMAIN;
import static com.crockgold.core.auth.controllers.AuthExceptionHandler.EMAIL_INVALID_LOCAL_PART;
import static com.crockgold.core.auth.controllers.AuthExceptionHandler.EMAIL_LOCAL_TOO_LONG;
import static com.crockgold.core.auth.controllers.AuthExceptionHandler.EMAIL_NOT_EXACTLY_ONE_AT_SIGN;
import static com.crockgold.core.auth.controllers.AuthExceptionHandler.EMAIL_TOO_LONG;
import static com.crockgold.core.auth.controllers.AuthExceptionHandler.INVALID_CREDENTIALS;
import static com.crockgold.core.auth.controllers.AuthExceptionHandler.PASSWORD_INVALID_CHARACTERS;
import static com.crockgold.core.auth.controllers.AuthExceptionHandler.PASSWORD_TOO_LONG;
import static com.crockgold.core.auth.controllers.AuthExceptionHandler.PASSWORD_TOO_SHORT;

import com.crockgold.core.auth.dto.LoginRequest;
import com.crockgold.core.auth.dto.RegisterRequest;
import com.crockgold.core.auth.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;

/**
 * Documentación OpenAPI de {@link AuthController}. Cada ejemplo de error es la respuesta real: el
 * {@code detail} sale de las constantes de {@link AuthExceptionHandler} o de Spring.
 */
@Tag(name = "Auth", description = "Registro e inicio de sesión de usuarios")
@SecurityRequirements
interface AuthApi {

  String PROBLEM_JSON = "application/problem+json";

  /** Mensajes de Spring: falla una validación del DTO o el JSON no se puede leer. */
  String MISSING_DATA = "Invalid request content.";
  String UNREADABLE_BODY = "Failed to read request";

  String DETAIL = "{\"detail\":\"";
  String REGISTER_400 = "\",\"instance\":\"/auth/register\",\"status\":400,\"title\":\"Bad Request\"}";
  String REGISTER_409 = "\",\"instance\":\"/auth/register\",\"status\":409,\"title\":\"Conflict\"}";
  String LOGIN_400 = "\",\"instance\":\"/auth/login\",\"status\":400,\"title\":\"Bad Request\"}";
  String LOGIN_401 = "\",\"instance\":\"/auth/login\",\"status\":401,\"title\":\"Unauthorized\"}";

  @Operation(summary = "Registra un usuario", description = "El email se normaliza (sin espacios laterales y en minúsculas) antes de guardarse.")
  @ApiResponse(responseCode = "201", description = "Usuario registrado")
  @ApiResponse(responseCode = "400", description = "Faltan datos, el JSON no es válido, o el email o la contraseña no cumplen las reglas", content = @Content(mediaType = PROBLEM_JSON, schema = @Schema(implementation = ProblemDetail.class), examples = {
      @ExampleObject(name = "Falta el email o la contraseña", value = DETAIL + MISSING_DATA
        + REGISTER_400),
      @ExampleObject(name = "JSON mal formado", value = DETAIL + UNREADABLE_BODY + REGISTER_400),
      @ExampleObject(name = "Email demasiado largo", value = DETAIL + EMAIL_TOO_LONG
        + REGISTER_400),
      @ExampleObject(name = "Email sin exactamente una arroba", value = DETAIL
        + EMAIL_NOT_EXACTLY_ONE_AT_SIGN + REGISTER_400),
      @ExampleObject(name = "Parte anterior a la arroba demasiado larga", value = DETAIL
        + EMAIL_LOCAL_TOO_LONG + REGISTER_400),
      @ExampleObject(name = "Parte anterior a la arroba no válida", value = DETAIL
        + EMAIL_INVALID_LOCAL_PART + REGISTER_400),
      @ExampleObject(name = "Dominio no válido", value = DETAIL + EMAIL_INVALID_DOMAIN
        + REGISTER_400),
      @ExampleObject(name = "Contraseña demasiado corta", value = DETAIL + PASSWORD_TOO_SHORT
        + REGISTER_400),
      @ExampleObject(name = "Contraseña demasiado larga", value = DETAIL + PASSWORD_TOO_LONG
        + REGISTER_400),
      @ExampleObject(name = "Contraseña con caracteres no admitidos", value = DETAIL
        + PASSWORD_INVALID_CHARACTERS + REGISTER_400)}))
  @ApiResponse(responseCode = "409", description = "El email ya está registrado", content = @Content(mediaType = PROBLEM_JSON, schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Email ya registrado", value = DETAIL
    + EMAIL_ALREADY_REGISTERED + REGISTER_409)))
  UserResponse register(RegisterRequest request);

  @Operation(summary = "Inicia sesión", description = "No abre sesión ni devuelve token: solo comprueba las credenciales del usuario.")
  @ApiResponse(responseCode = "200", description = "Credenciales correctas")
  @ApiResponse(responseCode = "400", description = "Faltan datos, el JSON no es válido o el email no es una dirección válida", content = @Content(mediaType = PROBLEM_JSON, schema = @Schema(implementation = ProblemDetail.class), examples = {
      @ExampleObject(name = "Falta el email o la contraseña", value = DETAIL + MISSING_DATA
        + LOGIN_400),
      @ExampleObject(name = "JSON mal formado", value = DETAIL + UNREADABLE_BODY + LOGIN_400),
      @ExampleObject(name = "Email demasiado largo", value = DETAIL + EMAIL_TOO_LONG + LOGIN_400),
      @ExampleObject(name = "Email sin exactamente una arroba", value = DETAIL
        + EMAIL_NOT_EXACTLY_ONE_AT_SIGN + LOGIN_400),
      @ExampleObject(name = "Parte anterior a la arroba demasiado larga", value = DETAIL
        + EMAIL_LOCAL_TOO_LONG + LOGIN_400),
      @ExampleObject(name = "Parte anterior a la arroba no válida", value = DETAIL
        + EMAIL_INVALID_LOCAL_PART + LOGIN_400),
      @ExampleObject(name = "Dominio no válido", value = DETAIL + EMAIL_INVALID_DOMAIN
        + LOGIN_400)}))
  @ApiResponse(responseCode = "401", description = "El email o la contraseña no coinciden, sin indicar cuál", content = @Content(mediaType = PROBLEM_JSON, schema = @Schema(implementation = ProblemDetail.class), examples = @ExampleObject(name = "Credenciales incorrectas", value = DETAIL
    + INVALID_CREDENTIALS + LOGIN_401)))
  UserResponse login(LoginRequest request);
}
