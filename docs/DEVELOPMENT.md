# Entorno de desarrollo

Todas las tareas son de `make`. `make help` las lista con sus parámetros.

## Primera vez

En el host, con `bash`, `make` y Docker:

1. `make setup` crea `docker/.env`, detecta `DOCKER_GID` y genera las contraseñas.
2. Abrir la carpeta en VS Code y elegir **Reopen in Container**.

`make setup` es la única tarea que corre en el host. Repetirla no pisa lo ya configurado.

## Tareas diarias

Dentro del Dev Container:

| Tarea | Comando | Nota |
| ----- | ------- | ---- |
| Arrancar la API | `make run` | <http://localhost:8080> |
| Detener la API | `make stop` | |
| Probar la API | `make smoke` | health, `401` y `404` |
| Validar todo | `make check` | lo mismo que el Pipeline |
| Tests | `make test T='*Patron*'` | `T` es opcional |
| Corregir el formato | `make format` | |
| Ver informes | `make report` | tests y cobertura, puerto `8090` |

- Los tests levantan su propio `postgres:18` con Testcontainers, usando el Docker del host.
- `make check` imprime al final el resumen de cobertura.

## Base de datos local

| Tarea | Comando |
| ----- | ------- |
| Consola SQL | `make psql` |
| Listar las tablas | `make db-tables` |
| Historial de Flyway | `make db-migrations` |
| Copia de seguridad | `make db-dump` (queda en `dumps/`) |
| Restaurar una copia | `make db-restore FILE=dumps/<copia>.dump` |
| Vaciar la base | `make db-reset` |

- `db-restore` y `db-reset` piden confirmación.
- `dumps/` está fuera de git y `make clean` no la borra.

## Migraciones

- Se crean con `make migration NAME=<descripcion>`, que asigna el número siguiente.
- Viven en `src/main/resources/db/migration/` como `V<n>__<descripcion>.sql`. Hoy no existe ninguna.
- Flyway las aplica al arrancar. Si una falla, la aplicación no arranca.

Reglas:

1. Una migración publicada no se edita ni se borra, porque Flyway compara su checksum. Los cambios van en una nueva.
2. Cada migración debe funcionar con la versión anterior del código (añadir antes de quitar), para poder revertir la imagen.
3. Lo destructivo va en una versión posterior a la que dejó de usar lo que se borra.

## Contenedores

El Dev Container no tiene `docker`. Los contenedores se manejan en el host, desde la raíz del repositorio:

- Estado, logs y parada: `docker compose -f docker/compose.yml`, seguido de `ps`, `logs -f db` o `down`. `down` no borra datos.
- Borrar el volumen de la base: `docker volume rm crockgold_core_pgdata` tras `down`. Solo hace falta para que PostgreSQL vuelva a aplicar usuario y contraseña. Para vaciar los datos basta `make db-reset`.
- Los puertos `8080` y `5432` solo se publican en `127.0.0.1`.

### Probar la imagen de producción antes de un release

`docker/Dockerfile.prod` se construye por primera vez al publicar el tag, que ya despliega. Conviene probarla antes si el cambio toca ese archivo, las dependencias o la configuración de arranque.

En el host, con el compose levantado y los valores `<...>` tomados de `docker/.env`:

```sh
docker build -f docker/Dockerfile.prod -t crockgold_core:local .
docker run --rm --name crockgold_local --network crockgold_core_default \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/crockgold \
  -e SPRING_DATASOURCE_USERNAME=<POSTGRES_USER> \
  -e SPRING_DATASOURCE_PASSWORD=<POSTGRES_PASSWORD> \
  -e SPRING_SECURITY_USER_PASSWORD=<cualquiera> \
  crockgold_core:local
```

Después, en el Dev Container: `make smoke URL=http://crockgold_local:8080 USUARIO=user CLAVE=<cualquiera>`.

## Fallos habituales

| Síntoma | Qué hacer |
| ------- | --------- |
| `spotlessJavaCheck` falla | `make format` |
| Tests fallan en CI | Descargar el artefacto `reports` de la ejecución |
| Tests: `permission denied` en `/var/run/docker.sock` | `make setup` en el host y **Rebuild Container** |
| Tests: `Could not find a valid Docker environment` | Arrancar Docker en el host y **Rebuild Container** |
| `Port 8080 was already in use` | `make stop` |
| `password authentication failed` | `docker/.env` cambió después de crear el volumen: [rotar credenciales](CONFIGURATION.md#rotar-credenciales) o borrar el volumen |
| Compose: `port is already allocated` | Liberar el `8080` o el `5432` en el host |
| Gradle se comporta raro tras cambiar el build | `make gradle-reset` |
