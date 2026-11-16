package com.crockgold.core.openapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.List;
import java.util.Map;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Contrato OpenAPI de la API. Toda operación pide la credencial HTTP Basic salvo las que declaran
 * {@code @SecurityRequirements()} y el estado de salud, igual que la seguridad (ADR 0006).
 */
@Configuration(proxyBeanMethods = false)
class OpenApiConfiguration {

  static final String API_CREDENTIAL = "apiCredential";
  private static final String HEALTH_PATH = "/actuator/health";

  @Bean
  OpenAPI openApi() {
    return new OpenAPI()
      .info(new Info().title("Crockgold Core API")
        .version("v1")
        .description("Motor económico para mundos virtuales."))
      .components(new Components().addSecuritySchemes(API_CREDENTIAL,
        new SecurityScheme().type(SecurityScheme.Type.HTTP)
          .scheme("basic")
          .description("Credencial de la API (SPRING_SECURITY_USER_*)")))
      .addSecurityItem(new SecurityRequirement().addList(API_CREDENTIAL));
  }

  @Bean
  OpenApiCustomizer publicHealth() {
    return openApi -> openApi.getPaths()
      .entrySet()
      .stream()
      .filter(path -> path.getKey().startsWith(HEALTH_PATH))
      .flatMap(path -> path.getValue().readOperations().stream())
      .forEach(operation -> operation.setSecurity(List.of()));
  }

  /** Actuator no declara la forma de la respuesta: se documenta la que devuelve la API. */
  @Bean
  OpenApiCustomizer healthExample() {
    return openApi -> {
      Operation get = openApi.getPaths().get(HEALTH_PATH).getGet();
      Object example = Map.of("status", "UP", "groups", List.of("liveness", "readiness"));
      get.getResponses()
        .get("200")
        .getContent()
        .values()
        .forEach(mediaType -> mediaType.setExample(example));
    };
  }

  /**
   * Spring serializa {@code ProblemDetail} sin el campo {@code properties}: sus entradas van al
   * nivel superior. Además omite {@code type} cuando vale {@code about:blank}.
   */
  @Bean
  OpenApiCustomizer problemDetailSchema() {
    return openApi -> {
      Schema<?> problem = openApi.getComponents().getSchemas().get("ProblemDetail");
      problem.getProperties().remove("properties");
      problem.getProperties()
        .get("type")
        .description("Se omite cuando es about:blank, el valor por defecto");
    };
  }
}
