package com.crockgold.core.security;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.security.autoconfigure.SecurityProperties;

class SecurityConfigurationTests {

  @Test
  @DisplayName("La aplicación no arranca sin la contraseña de la API")
  void startupFailsWithoutConfiguredPassword() {
    SecurityProperties withoutPassword = new SecurityProperties();

    assertThatIllegalStateException()
      .isThrownBy(() -> new SecurityConfiguration().apiUser(withoutPassword))
      .withMessageContaining("SPRING_SECURITY_USER_PASSWORD");
  }

  @Test
  @DisplayName("La aplicación no arranca con la contraseña de la API en blanco")
  void startupFailsWithBlankPassword() {
    SecurityProperties blankPassword = new SecurityProperties();
    blankPassword.getUser().setPassword("   ");

    assertThatIllegalStateException()
      .isThrownBy(() -> new SecurityConfiguration().apiUser(blankPassword))
      .withMessageContaining("SPRING_SECURITY_USER_PASSWORD");
  }
}
