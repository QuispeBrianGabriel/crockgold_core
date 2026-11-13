#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Vacía por completo la base LOCAL: borra el esquema `public` y lo recrea.
# Flyway vuelve a aplicar todas las migraciones al siguiente `make run`.
#
# Solo funciona contra la base local, a propósito: no acepta otra URL.
# No cambia usuarios ni contraseñas (eso vive fuera del esquema).
# Pide confirmación porque no tiene vuelta atrás.
# -----------------------------------------------------------------------------
source "$(dirname "$0")/_common.sh"

use_local_db

confirm "Se borrarán TODOS los datos de $(redact "$DB"). ¿Seguir?"

if ! psql --quiet --dbname="$DB" --command 'DROP SCHEMA public CASCADE; CREATE SCHEMA public;'; then
	fail "No se pudo vaciar la base (revisa el error de arriba)"
fi
ok "Base vacía"
