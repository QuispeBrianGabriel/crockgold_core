# 0004 — Tests contra un PostgreSQL real con Testcontainers

- Estado: Aceptado
- Fecha: 2026-11-10

## Contexto

Los tests arrancan la aplicación completa, y esta necesita una base de datos.

## Decisión

- Los tests levantan un contenedor **`postgres:18`** con **Testcontainers**.
- Spring Boot se conecta a ese contenedor de forma automática con `@ServiceConnection`.
- Los tests que necesitan base de datos importan `TestcontainersConfiguration`.

## Consecuencias

- Los tests usan la misma base de datos de desarrollo y producción.
- Se requiere de Docker para ejecutar los tests.
- Dentro del devcontainer, los tests usan el Docker del host.

Por eso es que `compose.yml` monta `/var/run/docker.sock` y define `TESTCONTAINERS_HOST_OVERRIDE` y `DOCKER_GID`.

## Dónde verlo

- `src/test/java/com/crockgold/core/TestcontainersConfiguration.java`
- `src/test/java/com/crockgold/core/CrockgoldCoreApplicationTests.java`
- `docker/compose.yml`
- `docker/.env.example`
