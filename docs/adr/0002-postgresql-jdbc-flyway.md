# 0002 — PostgreSQL con Spring Data JDBC y Flyway

- Estado: Aceptado
- Fecha: 2026-11-10

## Contexto

El servicio necesita una base de datos relacional y una forma ordenada de cambiar su esquema.

## Decisión

- La base de datos es **PostgreSQL 18**.
- El acceso a datos se hace con **Spring Data JDBC**.
- Los cambios de esquema se aplican con **Flyway**.
- La conexión se configura con las variables `SPRING_DATASOURCE_*`.

## Consecuencias

- La misma versión de PostgreSQL (`postgres:18`) en desarrollo y en tests.
- No hay credenciales de base de datos en el código.

## Dónde verlo

`build.gradle`, `docker/compose.yml` y `docker/.env.example`.
