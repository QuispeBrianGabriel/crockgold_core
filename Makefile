# =============================================================================
#  Crockgold Core — interfaz del desarrollador (ver docs/adr/0009)
#
#  Ejecuta `make` o `make help` para ver todas las tareas.
#  La lógica vive en scripts/: aquí solo se llaman comandos.
#
#  Todas las tareas se ejecutan dentro del Dev Container, salvo `setup`,
#  que se ejecuta en el host antes de abrirlo por primera vez.
#
#  Al añadir una tarea, añade también su línea en `help`.
# =============================================================================

# bash y no sh (dash): con un solo comando, bash se reemplaza por él. dash se
# queda en medio y, tras un Ctrl+C, se mata con SIGINT aunque el comando haya
# terminado bien, y make imprime "*** Interrupt" (ver scripts/graceful.sh).
SHELL         := bash
.DEFAULT_GOAL := help
MAKEFLAGS     += --no-print-directory

# Envoltorio de cierre ordenado con Ctrl+C (ver scripts/graceful.sh)
GRACEFUL := scripts/graceful.sh
GRADLE   := $(GRACEFUL) "🛑 Tarea de Gradle interrumpida" ./gradlew

# Parámetros opcionales (se pasan como `make tarea VAR=valor`)
T       ?=
NAME    ?=
FILE    ?=
V       ?=
DB      ?=
URL     ?= http://localhost:8080
USUARIO ?= $(SPRING_SECURITY_USER_NAME)
CLAVE   ?= $(SPRING_SECURITY_USER_PASSWORD)

# Consola SQL contra la base local, con las mismas variables que usa la API
PSQL := env PGPASSWORD="$(SPRING_DATASOURCE_PASSWORD)" psql --username="$(SPRING_DATASOURCE_USERNAME)" "$(subst jdbc:,,$(SPRING_DATASOURCE_URL))"

.PHONY: setup help tree secret docs-lint \
        run stop health smoke \
        check test format format-check coverage report \
        compile build clean gradle-reset \
        psql db-tables db-migrations migration db-dump db-restore db-reset \
        version release

# =============================================================================
#  📚 Ayuda
# =============================================================================

# Formatos de printf usados por `help`:
#   TITLE → título de grupo
#   ITEM  → tarea + descripción
#   ARGS  → tarea + descripción + parámetros (atenuados)
TITLE := \n\033[1;33m%s\033[0m\n
ITEM  := "  \033[1;36m%-15s\033[0m %s\n"
ARGS  := "  \033[1;36m%-15s\033[0m %s \033[2m%s\033[0m\n"

help:
	@printf "\n\033[1;35m  🪙  Crockgold Core\033[0m \033[2m— make <tarea> [VAR=valor]\033[0m\n"

	@printf "$(TITLE)" "🖥️  Host — se ejecuta FUERA del Dev Container"
	@printf $(ITEM) "setup"         "🧰 Prepara el repo recién clonado (una sola vez)"

	@printf "$(TITLE)" "📚 General"
	@printf $(ITEM) "help"          "📖 Muestra esta ayuda"
	@printf $(ITEM) "tree"          "🌳 Árbol del código del proyecto"
	@printf $(ITEM) "secret"        "🔑 Genera una contraseña aleatoria"
	@printf $(ITEM) "docs-lint"     "📝 Valida enlaces y formato de docs/"

	@printf "$(TITLE)" "🚀 Aplicación"
	@printf $(ITEM) "run"           "▶️  Arranca la API en :8080"
	@printf $(ITEM) "stop"          "⏹️  Detiene la API y libera el 8080"
	@printf $(ARGS) "health"        "💓 Consulta /actuator/health"
	@printf $(ARGS) "smoke"         "🔥 Comprueba health, 401 y 404"

	@printf "$(TITLE)" "🧪 Calidad"
	@printf $(ITEM) "check"         "✅ Lo mismo que el pipeline"
	@printf $(ARGS) "test"          "🧪 Ejecuta los tests"
	@printf $(ITEM) "format"        "🎨 Corrige el formato"
	@printf $(ITEM) "format-check"  "🔍 Verifica el formato sin tocar nada"
	@printf $(ITEM) "coverage"      "📊 Tests + resumen de cobertura"
	@printf $(ITEM) "report"        "📄 Abre tests y cobertura en el navegador"

	@printf "$(TITLE)" "🏗️  Build"
	@printf $(ITEM) "compile"       "⚙️  Compila sin ejecutar tests"
	@printf $(ITEM) "build"         "📦 Genera el JAR en build/libs/"
	@printf $(ITEM) "clean"         "🧹 Borra build/"
	@printf $(ITEM) "gradle-reset"  "♻️  Para Gradle y borra su caché"

	@printf "$(TITLE)" "🐘 Base de datos (local)"
	@printf $(ITEM) "psql"          "💻 Consola SQL"
	@printf $(ITEM) "db-tables"     "📋 Lista las tablas"
	@printf $(ITEM) "db-migrations" "🗂️  Historial de Flyway"
	@printf $(ARGS) "migration"     "✍️  Crea la siguiente migración"
	@printf $(ITEM) "db-dump"       "💾 Copia de seguridad en dumps/"
	@printf $(ARGS) "db-restore"    "⏪ Restaura una copia (pide confirmar)"
	@printf $(ITEM) "db-reset"      "💣 Vacía la base (pide confirmar)"

	@printf "$(TITLE)" "🏷️  Release"
	@printf $(ITEM) "version"       "🔖 Última versión publicada"
	@printf $(ARGS) "release"       "🚢 Publica y despliega (pide confirmar)"

	@printf "\n\033[2m  Docs: docs/README.md\033[0m\n\n"

.DEFAULT:
	@printf "\033[1;31m✘\033[0m La tarea '%s' no existe. Usa \033[1;36mmake help\033[0m para ver todas.\n" "$@"

# Único comando que se ejecuta en el host
setup:
	@scripts/setup.sh

tree:
	@$(GRACEFUL) "🛑 Cancelado" tree -a --dirsfirst -I '.git|.gradle|build|bin|dumps|docs|.github|scripts|gradle|docker|.devcontainer|.vscode'

docs-lint:
	@scripts/docs-lint.sh

secret:
	@$(GRACEFUL) "🛑 Cancelado" openssl rand -base64 32

# =============================================================================
#  🚀 Aplicación
# =============================================================================

run:
	@$(GRACEFUL) "⏹️  API detenida correctamente" ./gradlew bootRun

stop:
	@scripts/stop.sh

health:
	@$(GRACEFUL) "🛑 Consulta cancelada" curl -sS -w '\n' $(URL)/actuator/health

smoke:
	@scripts/smoke.sh "$(URL)" "$(USUARIO)" "$(CLAVE)"

# =============================================================================
#  🧪 Calidad
# =============================================================================

check:
	@$(GRADLE) check

# Con T se pasa el filtro a Gradle: make test T='*Security*'
test:
	@$(GRADLE) test $(if $(T),--tests '$(T)')

format:
	@$(GRADLE) spotlessApply

format-check:
	@$(GRADLE) spotlessCheck

coverage:
	@$(GRADLE) test jacocoTestReport

report:
	@scripts/report.sh

# =============================================================================
#  🏗️ Build
# =============================================================================

compile:
	@$(GRADLE) classes testClasses

build:
	@$(GRADLE) bootJar

clean:
	@$(GRADLE) clean

gradle-reset:
	@$(GRADLE) --stop
	@$(GRACEFUL) "🛑 Cancelado" rm -rf .gradle/configuration-cache

# =============================================================================
#  🐘 Base de datos (local)
# =============================================================================

# Mensaje vacío: en psql, Ctrl+C cancela la consulta, no cierra la sesión
psql:
	@$(GRACEFUL) "" $(PSQL)

db-tables:
	@$(GRACEFUL) "🛑 Consulta cancelada" $(PSQL) -c '\dt'

db-migrations:
	@$(GRACEFUL) "🛑 Consulta cancelada" $(PSQL) -c 'SELECT version, description, installed_on, success FROM flyway_schema_history ORDER BY installed_rank;'

migration:
	@scripts/new-migration.sh "$(NAME)"

# Con DB se apunta a otra base (ej. producción): make db-dump DB='postgresql://…'
db-dump:
	@scripts/db-dump.sh "$(DB)"

db-restore:
	@scripts/db-restore.sh "$(FILE)" "$(DB)"

db-reset:
	@scripts/db-reset.sh

# =============================================================================
#  🏷️ Release
# =============================================================================

version:
	@$(GRACEFUL) "🛑 Cancelado" git describe --tags --abbrev=0

release:
	@scripts/release.sh "$(V)"
