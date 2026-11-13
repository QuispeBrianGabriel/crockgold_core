#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Ejecuta un comando del Makefile con salida ordenada (ver _common.sh):
#   - Ctrl+C          → muestra el mensaje indicado.
#   - El comando falla → explica qué falló y con qué código.
#   - No existe        → sugiere reconstruir el Dev Container.
# Nunca termina con un error de make.
#
# Uso: scripts/graceful.sh "<mensaje al pulsar Ctrl+C>" <comando> [args…]
#   Ej:  scripts/graceful.sh "⏹️  API detenida" ./gradlew bootRun
#
# Con mensaje vacío ("") el script ignora Ctrl+C y deja que lo gestione el
# propio comando. Es lo que necesita psql: ahí Ctrl+C cancela la consulta en
# curso y la sesión sigue abierta, no es un cierre.
# -----------------------------------------------------------------------------
source "$(dirname "$0")/_common.sh"

INTERRUPT_MSG="$1"
shift

if [[ -z "$INTERRUPT_MSG" ]]; then
	trap "" INT
fi

# Nombre legible del programa para los mensajes: se saltan `env` y las
# asignaciones VAR=valor (así nunca se imprime PGPASSWORD=…).
program=""
for arg in "$@"; do
	if [[ "$arg" != env && "$arg" != *=* ]]; then
		program=$(basename "$arg")
		break
	fi
done

status=0
"$@" || status=$?

case $status in
0) ;;
127) fail "No se encuentra el comando '$program': haz Rebuild Container para instalarlo" ;;
*) fail "$program terminó con errores (código $status): revisa la salida de arriba" ;;
esac
