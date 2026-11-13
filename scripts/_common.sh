#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Utilidades compartidas por los scripts de esta carpeta.
# No se ejecuta solo: cada script lo carga con `source`.
#
#   info "texto"     → mensaje informativo (azul)
#   ok "texto"       → éxito (verde)
#   fail "texto"     → error explicado (rojo) y termina el script
#   confirm "texto"  → pregunta [s/N]; si la respuesta no es "s", cancela
#   at_exit función  → registra una limpieza que se ejecuta siempre al salir
#
# Además instala el manejo de salida (ver "Salida ordenada" más abajo):
# todo script termina con un mensaje propio y nunca con un error de make.
# -----------------------------------------------------------------------------

# Detiene el script ante cualquier error, variable no definida o fallo en un pipe.
# Esos errores no quedan sin explicar: los recoge on_exit.
set -euo pipefail

RESET=$'\033[0m'
BOLD=$'\033[1m'
DIM=$'\033[2m'
RED=$'\033[31m'
GREEN=$'\033[32m'
YELLOW=$'\033[33m'
BLUE=$'\033[34m'

info() {
	printf '%s▶%s %s\n' "$BLUE$BOLD" "$RESET" "$1"
}

ok() {
	printf '%s✔%s %s\n' "$GREEN$BOLD" "$RESET" "$1"
}

# El error ya queda explicado con el mensaje, así que se sale con 0:
# make no añade su "make: *** Error 1".
fail() {
	printf '%s✘%s %s\n' "$RED$BOLD" "$RESET" "$1" >&2
	exit 0
}

confirm() {
	local answer
	read -r -p "${YELLOW}${BOLD}⚠ $1 [s/N]${RESET} " answer
	if [[ ! "$answer" =~ ^[sS]$ ]]; then
		printf '%s🚫 Cancelado: no se ha hecho nada%s\n' "$DIM" "$RESET"
		exit 0
	fi
}

# =============================================================================
#  Salida ordenada
#
#  Regla del proyecto: ningún `make <tarea>` termina con un error de make ni
#  con un error sin explicar. Cada script sale de una de estas formas:
#
#   1. Bien                  → termina normalmente.
#   2. Error previsto        → fail "explicación".
#   3. Ctrl+C                → on_interrupt muestra INTERRUPT_MSG.
#   4. Error no previsto     → on_exit muestra qué comando falló y su código.
#
#  En todos los casos el código de salida final es 0, para que make no
#  imprima su propio mensaje. A cambio, `make` no sirve para encadenar
#  comandos por su código (el Pipeline usa ./gradlew directamente).
# =============================================================================

# Limpiezas registradas con at_exit (ej. cerrar un servidor, borrar un tag).
CLEANUPS=()
at_exit() {
	CLEANUPS+=("$1")
}

# El trap ERR salta justo antes de que `set -e` termine el script: guarda el
# comando que falló para poder contarlo en on_exit. $BASH_COMMAND es el texto
# sin expandir, así que no muestra contraseñas.
FAILED_CMD=""
trap 'FAILED_CMD=$BASH_COMMAND' ERR

on_exit() {
	local status=$?
	# Un segundo Ctrl+C mientras se limpia no debe cortar la limpieza.
	trap "" INT TERM

	local cleanup
	for cleanup in ${CLEANUPS[@]+"${CLEANUPS[@]}"}; do
		"$cleanup" || true
	done

	if [[ $status -ne 0 ]]; then
		printf '%s✘ Error inesperado (código %d)%s' "$RED$BOLD" "$status" "$RESET" >&2
		if [[ -n "$FAILED_CMD" ]]; then
			printf ' en: %s' "$FAILED_CMD" >&2
		fi
		printf '\n' >&2
	fi

	# `exit` dentro del trap EXIT fija el código final del script.
	exit 0
}
trap on_exit EXIT

# Ctrl+C envía SIGINT a todo el grupo: make, este script y el programa que
# esté corriendo (gradle, curl…). El programa se detiene solo; bash espera a
# que termine y entonces ejecuta este manejador. Cada script puede cambiar
# el mensaje asignando INTERRUPT_MSG.
INTERRUPT_MSG="🛑 Cancelado por el usuario"

on_interrupt() {
	trap "" INT TERM
	# El terminal ya mostró "^C": se salta de línea para no pegar el mensaje.
	printf '\n%s%s%s\n' "$YELLOW$BOLD" "$INTERRUPT_MSG" "$RESET"
	exit 0
}
trap on_interrupt INT TERM

# =============================================================================
#  Conexión a la base LOCAL (`db` del compose).
#
#  El Dev Container no define las variables PG*; se reutilizan las mismas
#  SPRING_DATASOURCE_* que usa la API, así solo hay una fuente de verdad.
#  Deja en $DB la URL de conexión y exporta PGUSER/PGPASSWORD para que
#  psql, pg_dump y pg_restore no pidan credenciales.
# =============================================================================
use_local_db() {
	if [[ -z "${SPRING_DATASOURCE_URL:-}" ]]; then
		fail "SPRING_DATASOURCE_URL no está definida: ¿estás dentro del Dev Container?"
	fi
	export PGUSER="${SPRING_DATASOURCE_USERNAME:-}"
	export PGPASSWORD="${SPRING_DATASOURCE_PASSWORD:-}"
	# jdbc:postgresql://db:5432/crockgold → postgresql://db:5432/crockgold
	DB="${SPRING_DATASOURCE_URL#jdbc:}"
}

# Muestra una URL de conexión sin usuario ni contraseña, para poder imprimirla.
# postgresql://user:clave@host/db → postgresql://host/db
redact() {
	sed -E 's#//[^@/]*@#//#' <<<"$1"
}
