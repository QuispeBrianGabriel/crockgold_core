# Configuración y credenciales

Todo se configura con variables de entorno. `application.yaml` no contiene nada que dependa del ambiente.

- Hay dos servicios: la API (`api` en local, `Core` en Railway) y PostgreSQL (`db` en local, `Postgres` en Railway).
- En local los valores salen de `docker/.env`, que `make setup` crea a partir de `docker/.env.example`.
- Tras cambiar `docker/.env` hay que hacer **Rebuild Container**.
- La configuración de Railway vive fuera del repositorio y esta página es su única copia.

## PostgreSQL

| Variable | Railway (`Postgres`) | Local (`db`) |
| -------- | -------------------- | ------------ |
| `POSTGRES_DB` | `crockgold` | `crockgold` |
| `POSTGRES_USER` | `postgres` | `postgres` |
| `POSTGRES_PASSWORD` | Secreta | La genera `make setup` |
| `POSTGRES_HOST` | `${{RAILWAY_PRIVATE_DOMAIN}}` | No existe, el host es `db` |
| `POSTGRES_PORT` | `5432` | No existe |

`POSTGRES_HOST` y `POSTGRES_PORT` solo existen para que `Core` las referencie.

## API

| Variable | Railway (`Core`) | Local (`api`) |
| -------- | ---------------- | ------------- |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://${{Postgres.POSTGRES_HOST}}:${{Postgres.POSTGRES_PORT}}/${{Postgres.POSTGRES_DB}}` | `jdbc:postgresql://db:5432/crockgold` |
| `SPRING_DATASOURCE_USERNAME` | `${{Postgres.POSTGRES_USER}}` | Valor de `POSTGRES_USER` |
| `SPRING_DATASOURCE_PASSWORD` | `${{Postgres.POSTGRES_PASSWORD}}` | Valor de `POSTGRES_PASSWORD` |
| `SPRING_SECURITY_USER_NAME` | `user` | `dev` en la plantilla |
| `SPRING_SECURITY_USER_PASSWORD` | Secreta | La genera `make setup` |
| `PORT` | `8080` | No se usa |

- Todas son obligatorias salvo `SPRING_SECURITY_USER_NAME`, que por defecto es `user`.
- Sin `SPRING_SECURITY_USER_PASSWORD` la aplicación no arranca.
- La aplicación no lee `PORT`. Railway la usa para saber a qué puerto enviar el tráfico.
- En Railway la conexión a PostgreSQL va por la red privada.
- `make psql` y las tareas `db-*` usan estas mismas `SPRING_DATASOURCE_*`. No hay variables `PG*`.

## Rotar credenciales

| Credencial | Pasos |
| ---------- | ----- |
| HTTP Basic | Cambiar `SPRING_SECURITY_USER_PASSWORD` y volver a desplegar. En local, **Rebuild Container** |
| PostgreSQL | 1. `ALTER USER <usuario> PASSWORD '...'` (en local, desde `make psql`). 2. Cambiar `POSTGRES_PASSWORD`. 3. Reiniciar la API |

- En PostgreSQL cambiar la variable no basta, porque la imagen solo la aplica al crear el volumen.
- `make secret` genera una contraseña nueva.
- Los secretos nunca se suben al repositorio. Uno filtrado se rota: borrarlo del historial no basta.
