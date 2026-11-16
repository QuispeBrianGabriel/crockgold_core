package com.crockgold.core.integration.support;

import io.cucumber.java.Before;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;

/** Cada escenario empieza con la base recién migrada. */
@RequiredArgsConstructor
public class CleanDatabase {

  private final Flyway flyway;

  @Before
  public void rerunMigrations() {
    flyway.clean();
    flyway.migrate();
  }
}
