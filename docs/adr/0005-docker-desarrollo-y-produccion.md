# 0005 — Docker para desarrollar y para producción

- Estado: Aceptado
- Fecha: 2026-11-12

## Contexto

Hace falta el mismo entorno para cualquiera que abra el proyecto y una imagen pequeña para desplegar.

## Decisión

Todo lo relacionado con Docker vive en `docker/`:

- **`Dockerfile.dev`**: entorno de desarrollo con JDK 25, git, curl, make, tree y el cliente de PostgreSQL.
- **`compose.yml`**: levanta `api` (entorno de desarrollo) y `db` (PostgreSQL 18). El Dev Container de VS Code usa este mismo archivo.
- **`Dockerfile.prod`**: dos etapas. La primera empaqueta el JAR con el JDK. La segunda lleva solo el JRE y el JAR, y ejecuta con un usuario sin privilegios.

## Consecuencias

- Abrir el Dev Container deja listos Java, la base de datos y las extensiones del editor.
- La imagen de producción no contiene Gradle ni el código fuente.
- Los datos de PostgreSQL y la caché de Gradle viven en volúmenes y sobreviven a reinicios.
- Las variables se leen de `docker/.env`, que no se sube a git.

## Dónde verlo

`docker/` y `.devcontainer/devcontainer.json`.
