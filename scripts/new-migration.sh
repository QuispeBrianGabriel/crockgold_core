#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Crea la siguiente migración de Flyway vacía, con el número correlativo.
#
# Uso: scripts/new-migration.sh <nombre>
#   Ej:  scripts/new-migration.sh crear_wallets
#   →    src/main/resources/db/migration/V3__crear_wallets.sql
#
# Recuerda las reglas de docs/DEVELOPMENT.md#migraciones: una migración
# publicada nunca se edita ni se borra.
# -----------------------------------------------------------------------------
source "$(dirname "$0")/_common.sh"

DIR=src/main/resources/db/migration
NAME="${1:-}"

if [[ -z "$NAME" ]]; then
	fail "Falta el nombre. Ej: make migration NAME=crear_wallets"
fi
if [[ ! "$NAME" =~ ^[a-z0-9_]+$ ]]; then
	fail "El nombre solo admite minúsculas, números y _"
fi

# Busca el número más alto entre los archivos V<n>__*.sql existentes.
# sed extrae solo el <n>; sort -n ordena numéricamente (para que V10 > V9).
# En un clon nuevo la carpeta no existe: git no guarda carpetas vacías.
mkdir -p "$DIR"

last=$(ls "$DIR" | sed -n 's/^V\([0-9]\+\)__.*/\1/p' | sort -n | tail -1)
next=$(( ${last:-0} + 1 ))

file="$DIR/V${next}__${NAME}.sql"
printf -- '-- %s\n' "$NAME" > "$file"
ok "Creada $file"
