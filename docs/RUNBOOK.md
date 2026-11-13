# Runbook

Qué hacer cuando producción falla. Primero se restaura el servicio y después se diagnostica.

| Dato | Valor |
| ---- | ----- |
| URL de producción | **Por confirmar** |
| Responsable y canal de aviso | **Por confirmar** |
| Plataforma | Railway, con los servicios `Core` y `Postgres` |
| Imagen de `Core` | `ghcr.io/quispebriangabriel/crockgold_core:latest` |
| Logs | Solo en Railway, por salida estándar |

`Core` no guarda estado, así que reiniciarlo o reemplazarlo siempre es seguro. Todo el estado está en `Postgres`.

## 1. Confirmar el fallo

Ejecutar `make health URL=<url>`:

| Respuesta | Significado | Qué hacer |
| --------- | ----------- | --------- |
| `200` y `"status":"UP"` | Proceso vivo y PostgreSQL accesible | Buscar en [Síntomas](#3-síntomas) |
| `503` y `"status":"DOWN"` | El proceso corre, PostgreSQL no responde | Recuperar `Postgres`. La aplicación reconecta sola |
| Sin respuesta | El proceso no está arriba | [Revertir](#2-revertir-a-la-versión-anterior) si hubo un release reciente. Si no, [Síntomas](#3-síntomas) |

## 2. Revertir a la versión anterior

Si el fallo empezó tras un release, se revierte sin esperar a entender la causa.

1. En el servicio `Core` de Railway, una de las dos:
    - Volver a desplegar el despliegue anterior desde el historial.
    - Fijar la imagen a la versión anterior de [Releases](https://github.com/QuispeBrianGabriel/crockgold_core/releases), por ejemplo `ghcr.io/quispebriangabriel/crockgold_core:0.3.2`.
2. Repetir `make health URL=<url>` hasta ver `UP`.

- Ninguna de las dos opciones se ha ensayado todavía.
- Revertir la imagen **no** deshace las migraciones: la versión anterior arranca contra el esquema nuevo.

## 3. Síntomas

Un arranque correcto termina con `Started CrockgoldCoreApplication in N seconds`.

| Síntoma | Causa probable | Acción |
| ------- | -------------- | ------ |
| Log: `Connection refused` o `password authentication failed` | `Postgres` caído o `SPRING_DATASOURCE_*` incorrectas | Revisar `Postgres` y las [variables de `Core`](CONFIGURATION.md#api) |
| Log: `Migration ... failed` o `checksum mismatch` | Migración defectuosa o editada tras publicarse | [Revertir](#2-revertir-a-la-versión-anterior) y corregir con una migración nueva |
| Log: `API credential missing` | Falta `SPRING_SECURITY_USER_PASSWORD` | Definirla en `Core` |
| `401` con credenciales que funcionaban | Cambiaron `SPRING_SECURITY_USER_*` | Revisar las variables de `Core` y volver a desplegar |
| Log: `Tomcat started on port 8080`, pero no responde | Railway envía el tráfico a otro puerto | Revisar que `PORT` sea `8080` |
| Release terminado y Railway no desplegó | Railway no detectó el cambio de `:latest` | Forzar un despliegue de `Core` |

## 4. Restaurar la base de datos

**No hay copias de seguridad definidas.** Hoy solo se puede restaurar si alguien hizo antes un `make db-dump` a mano.

- Se hace desde el Dev Container, que trae el cliente de PostgreSQL 18.
- `<db>` es la URL de conexión: `postgresql://usuario:clave@host:5432/crockgold`.
- Por defecto esa URL solo es accesible desde la red privada de Railway.

```sh
make db-dump DB=<db>                              # guarda primero el estado actual en dumps/
make db-restore DB=<db> FILE=dumps/<copia>.dump   # reemplaza el contenido (pide confirmación)
```

## 5. Después de la incidencia

1. Corregir en `main` con `git revert <commit-malo>` y [publicar](RELEASE.md) el siguiente número.
2. Si en Railway se fijó una versión concreta, volver a poner `:latest`.
3. Si se filtró una credencial, [rotarla](CONFIGURATION.md#rotar-credenciales).
4. Anotar en [Síntomas](#3-síntomas) el síntoma nuevo y lo que lo resolvió.
