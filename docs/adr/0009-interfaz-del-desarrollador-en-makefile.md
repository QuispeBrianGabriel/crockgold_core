# 0009 — Interfaz del desarrollador en un `Makefile`, lógica en `scripts/`

- Estado: Aceptado
- Fecha: 2026-11-13

## Contexto

Las tareas diarias estaban repartidas entre `./gradlew`, `psql`, `git`, `curl` y pasos manuales, cada una con sus opciones.
Había que recordarlas o buscarlas, y la puesta en marcha tras clonar exigía varios pasos a mano.

## Decisión

- Todo comando útil es una tarea de `make` en el `Makefile` de la raíz. `make` sin argumentos muestra `make help`.
- La documentación muestra la tarea de `make`, no el comando que hay debajo.
- Las tareas corren dentro del Dev Container. La única excepción es `make setup`, que prepara el host.
- El `Makefile` no contiene lógica: cada tarea es un comando directo o un script de `scripts/`, uno por tarea.
- `scripts/_common.sh` reúne lo compartido: mensajes, confirmaciones y la conexión a la base local.
- Ninguna tarea termina con un error de `make`. Cada salida se explica con un mensaje propio y el código final es 0.
- Queda fuera el ciclo de vida de los contenedores, que sigue en el host con `docker` porque el Dev Container no tiene ese cliente.

## Consecuencias

- Basta recordar `make help` para descubrir cualquier tarea.
- `release`, `db-restore` y `db-reset` validan antes de actuar y piden confirmación.
- `make release` es lo único que crea tags y no deja ninguno sin publicar.
- Añadir una tarea exige tocar dos sitios del `Makefile`: la tarea y su línea en `help`.
- Un comando nuevo en el `Makefile` va envuelto en `$(GRACEFUL)` o es un script.
- `make` no sirve para encadenar comandos por su código de salida. El Pipeline y cualquier automatización llaman a `./gradlew check`.

## Dónde verlo

`Makefile`, `scripts/` y `docker/Dockerfile.dev`.
