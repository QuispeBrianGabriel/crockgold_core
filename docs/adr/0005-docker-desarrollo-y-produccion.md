# 0005 — Docker para desarrollar y para producción

- Estado: Aceptado
- Fecha: 2026-11-12

## Contexto

Se requiere el mismo entorno para cualquiera que abra el proyecto, y una imagen de menor tamaño para desplegar.

## Decisión

Todo lo relacionado con Docker vive en la carpeta `docker/`.

- **`Dockerfile.dev`**: entorno de desarrollo con JDK 25, git, curl y el cliente de PostgreSQL.
- **`compose.yml`**: levanta el servicio `api` (entorno de desarrollo) y `db` (PostgreSQL 18).
- **`Dockerfile.prod`**: construcción en dos etapas.
    1. Empaqueta el JAR con el JDK.
    2. Lleva el JRE y el JAR, y ejecuta con un usuario sin privilegios.

El **Dev Container** de VS Code usa ese mismo `compose.yml`.

## Consecuencias

- Abrir el proyecto en el Dev Container deja listo Java, la base de datos y las extensiones del editor.
- La imagen de producción no contiene Gradle ni el código fuente.
- Los puertos `8080` y `5432` solo se publican en `127.0.0.1`.
- Los datos de PostgreSQL y la caché de Gradle se guardan en volúmenes y sobreviven a reinicios.
- Las variables se leen de `docker/.env`, que no se sube a git; `docker/.env.example` sirve de plantilla.

## Dónde verlo

- `docker/Dockerfile.dev`
- `docker/Dockerfile.prod`
- `docker/compose.yml`
- `docker/.env.example`
- `.devcontainer/devcontainer.json`
