package com.crockgold.core.integration;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Ejecuta los escenarios de {@code features/}, configurados en junit-platform.properties. Los
 * marcados con {@code @wip} se omiten, y que no quede ninguno por ejecutar no es un fallo.
 */
@Suite(failIfNoTests = false)
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
class IntegrationSuite {
}
