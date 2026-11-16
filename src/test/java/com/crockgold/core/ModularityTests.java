package com.crockgold.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTests {

  private final ApplicationModules modules = ApplicationModules.of(CrockgoldCoreApplication.class);

  @Test
  @DisplayName("Los módulos solo dependen entre sí a través de sus API públicas y sin ciclos")
  void modulesRespectTheirBoundaries() {
    modules.verify();
  }
}
