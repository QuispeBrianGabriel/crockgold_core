package com.crockgold.core.integration.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

/** Traduce el lenguaje del acceso a la API a peticiones HTTP. */
@RequiredArgsConstructor
public class AccessSteps {

  private final MockMvcTester mvc;
  private boolean withCredential;
  private MvcTestResult response;

  @Given("un cliente sin credencial")
  public void clientWithoutCredential() {
    withCredential = false;
  }

  @Given("un cliente con la credencial de la API")
  public void clientWithCredential() {
    withCredential = true;
  }

  @When("consulta el estado de salud")
  public void getHealth() {
    get("/actuator/health");
  }

  @When("consulta la información de la aplicación")
  public void getInfo() {
    get("/actuator/info");
  }

  @When("consulta una operación que no existe")
  public void getUnknownOperation() {
    get("/auth/anything");
  }

  @Then("ve que la aplicación está disponible")
  public void seesApplicationIsUp() {
    assertThat(response).hasStatusOk().bodyJson().extractingPath("$.status").isEqualTo("UP");
  }

  @Then("se le deniega el acceso")
  public void accessIsDenied() {
    assertThat(response).hasStatus(UNAUTHORIZED);
  }

  @Then("se le permite el acceso")
  public void accessIsAllowed() {
    assertThat(response.getResponse().getStatus()).isNotIn(UNAUTHORIZED.value(),
      FORBIDDEN.value());
  }

  @Then("no se le abre ninguna sesión")
  public void noSessionIsOpened() {
    assertThat(response.getRequest().getSession(false)).isNull();
    assertThat(response).doesNotContainHeader("Set-Cookie");
  }

  private void get(String path) {
    var request = mvc.get().uri(path);
    if (withCredential)
      request.headers(headers -> headers.setBasicAuth("test", "test"));
    response = request.exchange();
  }
}
