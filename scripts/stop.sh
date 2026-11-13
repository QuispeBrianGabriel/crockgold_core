#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Detiene la API lanzada con `make run` (./gradlew bootRun) y libera el 8080.
# Si no había ninguna corriendo, lo avisa sin fallar.
# -----------------------------------------------------------------------------
source "$(dirname "$0")/_common.sh"

# pkill busca por la línea de comandos completa del proceso Java.
if pkill -f CrockgoldCoreApplication; then
	ok "API detenida"
else
	printf '%sNo había ninguna API corriendo%s\n' "$DIM" "$RESET"
fi
