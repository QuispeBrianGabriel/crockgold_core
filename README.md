# Crockgold Core

[![Pipeline](https://github.com/quispebriangabriel/crockgold_core/actions/workflows/pipeline.yml/badge.svg)](https://github.com/quispebriangabriel/crockgold_core/actions/workflows/pipeline.yml)
**Crockgold Core** es el motor fundamental diseñado para gobernar, proteger y analizar economías virtuales en videojuegos y mundos digitales. 

## El Problema
Construir una economía virtual confiable es un desafío técnico complejo. Los desarrolladores suelen tropezar con problemas críticos de concurrencia —como transacciones duplicadas, errores de duplicación de ítems o saldos negativos causados por acciones simultáneas de los usuarios—, lo que los obliga a reinventar infraestructura financiera y contable desde cero para cada nuevo proyecto.

## Qué hace Crockgold
Crockgold actúa como una autoridad económica independiente y altamente segura (un motor contable centralizado) que gestiona el flujo de valor y activos entre entornos virtuales:

- **Transacciones Atómicas:** Garantiza que cada intercambio, compra o transferencia de activos sea estrictamente consistente, previniendo condiciones de carrera (*race conditions*) y corrupción de datos bajo alta concurrencia.
- **Aislamiento Multi-Tenant:** Separa de forma segura las economías de diferentes mundos o administradores dentro de una misma infraestructura unificada.
- **Telemetría de Negocio:** Registra indicadores económicos en tiempo real, como la velocidad de circulación de activos, la concentración de riqueza y el volumen de transacciones.
- **Analista Económico con Inteligencia Artificial:** Incorpora un asistente inteligente especializado en revisar la salud macroeconómica del sistema, permitiendo a los administradores consultar el estado financiero de sus mundos y recibir diagnósticos de balance sin alterar el flujo transaccional.

## Visión
Proporcionar a los creadores una columna vertebral económica sólida, predecible y de grado industrial, permitiéndoles concentrarse por completo en el diseño de experiencias de juego mientras Crockgold se encarga de la precisión matemática, la seguridad y la consistencia del patrimonio digital.

## Flujo de trabajo, versionado y release
Todo se deriva de los commits, que siguen [Conventional Commits](https://www.conventionalcommits.org/es/v1.0.0/). Nadie edita versiones, tags ni changelogs a mano.

1. **Desarrollo:** se abre un PR a `main`. Su título y descripción se generan solos a partir de los commits.
2. **PR de release:** tras cada merge con commits `feat` o `fix`, [release-please](https://github.com/googleapis/release-please) abre o actualiza un PR `chore(main): release X.Y.Z` con la versión (`gradle.properties`) y el `CHANGELOG.md`.
3. **Publicación:** al mergear ese PR se crean el tag `vX.Y.Z` y el GitHub Release, y se publica la imagen `ghcr.io/quispebriangabriel/crockgold_core:<versión>`.

| Commit | Efecto en la versión |
|---|---|
| `fix: …` | patch |
| `feat: …` | minor |
| `feat!: …` o `BREAKING CHANGE:` | major (minor mientras la versión sea 0.x) |
| `docs`, `ci`, `build`, `chore`, `refactor`, `test` | no generan release |

La cobertura (JaCoCo) se imprime al ejecutar `./gradlew check` y aparece en el resumen de cada ejecución del pipeline; el reporte HTML queda en `build/reports/jacoco/test/html`.
