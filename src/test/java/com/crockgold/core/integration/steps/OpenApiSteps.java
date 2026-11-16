package com.crockgold.core.integration.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

/** Traduce el lenguaje de la documentación de la API a peticiones HTTP. */
@RequiredArgsConstructor
public class OpenApiSteps {

  private static final String REGISTER = "$.paths['/auth/register'].post";
  private static final String LOGIN = "$.paths['/auth/login'].post";
  private static final String HEALTH = "$.paths['/actuator/health'].get";

  private final MockMvcTester mvc;
  private MvcTestResult response;

  @When("un cliente con la credencial consulta la documentación de la API")
  public void getApiDocs() {
    get("/v3/api-docs");
  }

  @When("un cliente con la credencial abre la documentación en el navegador")
  public void getSwaggerUi() {
    get("/swagger-ui/index.html");
  }

  @Then("ve documentado el registro de usuarios")
  public void registerIsDocumented() {
    assertDocumented(REGISTER);
  }

  @Then("ve documentado el inicio de sesión")
  public void loginIsDocumented() {
    assertDocumented(LOGIN);
  }

  @Then("ve documentado el estado de salud")
  public void healthIsDocumented() {
    assertDocumented(HEALTH);
  }

  @Then("ve que el registro de usuarios no pide credencial")
  public void registerIsPublic() {
    assertPublic(REGISTER);
  }

  @Then("ve que el inicio de sesión no pide credencial")
  public void loginIsPublic() {
    assertPublic(LOGIN);
  }

  @Then("ve que el estado de salud no pide credencial")
  public void healthIsPublic() {
    assertPublic(HEALTH);
  }

  @Then("ve que las demás operaciones piden la credencial de la API")
  public void othersRequireCredential() {
    assertThat(response).bodyJson()
      .extractingPath("$.security[0].apiCredential")
      .asArray()
      .isEmpty();
    assertThat(response).bodyJson()
      .extractingPath("$.components.securitySchemes.apiCredential.scheme")
      .isEqualTo("basic");
  }

  @Then("ve la documentación de la API")
  public void seesSwaggerUi() {
    assertThat(response).hasStatusOk().hasContentTypeCompatibleWith(MediaType.TEXT_HTML);
  }

  private void assertDocumented(String operation) {
    assertThat(response).hasStatusOk().bodyJson().hasPath(operation);
  }

  private void assertPublic(String operation) {
    assertThat(response).bodyJson().extractingPath(operation + ".security").asArray().isEmpty();
  }

  private void get(String path) {
    response = mvc.get()
      .uri(path)
      .headers(headers -> headers.setBasicAuth("test", "test"))
      .exchange();
  }
}
