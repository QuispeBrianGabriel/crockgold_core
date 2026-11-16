package com.crockgold.core.integration.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

import com.crockgold.core.integration.support.Notation;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import tools.jackson.databind.json.JsonMapper;

/**
 * Traduce el lenguaje del registro y del inicio de sesión a la API HTTP. Cada escenario pasa en una
 * tabla los datos que comparten un mismo resultado, escritos en la notación de {@link Notation}.
 */
@RequiredArgsConstructor
public class AuthSteps {

  private final MockMvcTester mvc;
  private final JsonMapper json;
  private List<String> attemptedEmails;
  private List<MvcTestResult> responses;
  private List<MvcTestResult> simultaneousResponses;

  @Given("que los siguientes usuarios están registrados:")
  public void registered(List<Map<String, String>> users) {
    register(users);
    registeredWithThoseEmails();
  }

  @When("alguien se registra con cada uno de estos datos:")
  public void register(List<Map<String, String>> attempts) {
    attempt("/auth/register", attempts);
  }

  @When("dos personas se registran a la vez con el email {}")
  public void registerTwiceAtOnce(String email) {
    var first = CompletableFuture.supplyAsync(() -> post("/auth/register", email, "password-1"));
    var second = CompletableFuture.supplyAsync(() -> post("/auth/register", email, "password-2"));
    simultaneousResponses = List.of(first.join(), second.join());
  }

  @When("alguien inicia sesión con cada uno de estos datos:")
  public void login(List<Map<String, String>> attempts) {
    attempt("/auth/login", attempts);
  }

  @Then("queda registrado con cada uno de esos emails")
  public void registeredWithThoseEmails() {
    identifiedAs(CREATED, attemptedEmails);
  }

  @Then("queda registrado como cada uno de estos usuarios:")
  public void registeredAs(List<Map<String, String>> users) {
    identifiedAs(CREATED, emailsOf(users));
  }

  @Then("se le reconoce como cada uno de estos usuarios:")
  public void recognizedAs(List<Map<String, String>> users) {
    identifiedAs(OK, emailsOf(users));
  }

  @Then("solo una de ellas queda registrada")
  public void onlyOneIsRegistered() {
    assertThat(simultaneousResponses).extracting(result -> result.getResponse().getStatus())
      .containsExactlyInAnyOrder(CREATED.value(), CONFLICT.value());
  }

  @Then("se rechaza porque faltan datos")
  public void rejectedAsIncomplete() {
    rejected(BAD_REQUEST, "Invalid request content");
  }

  @Then("se rechaza porque el email es demasiado largo")
  public void rejectedAsEmailTooLong() {
    rejected(BAD_REQUEST, "Email must not exceed");
  }

  @Then("se rechaza porque el email no tiene exactamente una arroba")
  public void rejectedAsNotExactlyOneAtSign() {
    rejected(BAD_REQUEST, "Email must contain exactly one '@'");
  }

  @Then("se rechaza porque la parte anterior a la arroba es demasiado larga")
  public void rejectedAsLocalPartTooLong() {
    rejected(BAD_REQUEST, "Email local part must not exceed");
  }

  @Then("se rechaza porque la parte anterior a la arroba no es válida")
  public void rejectedAsInvalidLocalPart() {
    rejected(BAD_REQUEST, "Email local part is not valid");
  }

  @Then("se rechaza porque el dominio del email no es válido")
  public void rejectedAsInvalidDomain() {
    rejected(BAD_REQUEST, "Email domain is not valid");
  }

  @Then("se rechaza porque el email ya está registrado")
  public void rejectedAsAlreadyRegistered() {
    rejected(CONFLICT, "Email already registered");
  }

  @Then("se rechaza porque la contraseña es demasiado corta")
  public void rejectedAsPasswordTooShort() {
    rejected(BAD_REQUEST, "Password must have at least");
  }

  @Then("se rechaza porque la contraseña es demasiado larga")
  public void rejectedAsPasswordTooLong() {
    rejected(BAD_REQUEST, "Password must not exceed");
  }

  @Then("se rechaza porque la contraseña tiene caracteres no admitidos")
  public void rejectedAsPasswordWithInvalidCharacters() {
    rejected(BAD_REQUEST, "Password must contain only");
  }

  @Then("se rechaza porque las credenciales son incorrectas")
  public void rejectedAsInvalidCredentials() {
    rejected(UNAUTHORIZED, "Invalid email or password");
  }

  /** Hace un intento por cada fila de la tabla, en orden, y guarda sus respuestas. */
  private void attempt(String path, List<Map<String, String>> attempts) {
    attemptedEmails = emailsOf(attempts);
    responses = attempts.stream()
      .map(data -> post(path, data.get("email"), data.get("contraseña")))
      .toList();
  }

  private List<String> emailsOf(List<Map<String, String>> rows) {
    return rows.stream().map(row -> row.get("email")).toList();
  }

  private MvcTestResult post(String path, String email, String password) {
    Map<String, String> credentials = new HashMap<>();
    credentials.put("email", Notation.expand(email));
    credentials.put("password", Notation.expand(password));
    return mvc.post()
      .uri(path)
      .contentType(APPLICATION_JSON)
      .content(json.writeValueAsString(credentials))
      .exchange();
  }

  /**
   * Cada intento identifica a su usuario por el email esperado, en el mismo orden, y la respuesta
   * nunca expone su contraseña.
   */
  private void identifiedAs(HttpStatus status, List<String> emails) {
    assertThat(responses).hasSameSizeAs(emails);
    for (int i = 0; i < emails.size(); i++)
      assertThat(responses.get(i)).as(attemptedEmails.get(i))
        .hasStatus(status)
        .bodyJson()
        .extractingPath("$")
        .asMap()
        .containsOnlyKeys("id", "email")
        .containsEntry("email", Notation.expand(emails.get(i)));
  }

  /** Todos los intentos se rechazan por el mismo motivo. */
  private void rejected(HttpStatus status, String detail) {
    assertThat(responses).isNotEmpty();
    for (int i = 0; i < responses.size(); i++)
      assertThat(responses.get(i)).as(attemptedEmails.get(i))
        .hasStatus(status)
        .hasContentTypeCompatibleWith(APPLICATION_PROBLEM_JSON)
        .bodyJson()
        .extractingPath("$.detail")
        .asString()
        .startsWith(detail);
  }
}
