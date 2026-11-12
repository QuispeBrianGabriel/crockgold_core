# 0002 — PostgreSQL con Spring Data JDBC y Flyway

- Estado: Aceptado
- Fecha: 2026-11-10

## Contexto

El servicio necesita una base de datos relacional y una forma ordenada de cambiar su esquema.

## Decisión

- La base de datos es **PostgreSQL 18**.
- El acceso a datos se hace con **Spring Data JDBC**.
- Los cambios de esquema se aplican con **Flyway**.
- La conexión se configura con envs (`SPRING_DATASOURCE_*`).

## Consecuencias

- Misma versión de PostgreSQL en desarrollo y en tests (`postgres:18`).
- No hay credenciales de base de datos escritas en el código.

## Dónde verlo

- `build.gradle` (dependencias `data-jdbc`, `flyway` y `postgresql`)
- `docker/compose.yml`
- `docker/.env.example`
