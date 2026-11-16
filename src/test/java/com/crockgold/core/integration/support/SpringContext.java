package com.crockgold.core.integration.support;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

/** Aplicación completa con un PostgreSQL real, compartida por todos los escenarios. */
@CucumberContextConfiguration
@SpringBootTest(properties = {
    "spring.flyway.clean-disabled=false",
    "spring.security.user.name=test",
    "spring.security.user.password=test"})
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class SpringContext {
}
