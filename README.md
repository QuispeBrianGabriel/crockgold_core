# Crockgold Core

[![Pipeline](https://img.shields.io/github/actions/workflow/status/QuispeBrianGabriel/crockgold_core/pipeline.yml?branch=main&label=pipeline&logo=githubactions&logoColor=white)](https://github.com/QuispeBrianGabriel/crockgold_core/actions/workflows/pipeline.yml)
[![Coverage](https://codecov.io/gh/QuispeBrianGabriel/crockgold_core/branch/main/graph/badge.svg)](https://app.codecov.io/gh/QuispeBrianGabriel/crockgold_core)
[![Release](https://img.shields.io/github/v/release/QuispeBrianGabriel/crockgold_core?logo=github)](https://github.com/QuispeBrianGabriel/crockgold_core/releases/latest)
[![Release date](https://img.shields.io/github/release-date/QuispeBrianGabriel/crockgold_core)](https://github.com/QuispeBrianGabriel/crockgold_core/releases)
[![Last commit](https://img.shields.io/github/last-commit/QuispeBrianGabriel/crockgold_core)](https://github.com/QuispeBrianGabriel/crockgold_core/commits/main)
[![Commit activity](https://img.shields.io/github/commit-activity/m/QuispeBrianGabriel/crockgold_core)](https://github.com/QuispeBrianGabriel/crockgold_core/pulse)
[![License](https://img.shields.io/github/license/QuispeBrianGabriel/crockgold_core)](LICENSE)
[![Java](https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/25/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Conventional Commits](https://img.shields.io/badge/Conventional_Commits-1.0.0-FE5196?logo=conventionalcommits&logoColor=white)](https://www.conventionalcommits.org/es/v1.0.0/)

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

## Integración continua y releases
Se trabaja directo sobre `main`, con commits [Conventional Commits](https://www.conventionalcommits.org/es/v1.0.0/). Cada push ejecuta el pipeline (`./gradlew check`: formato, análisis estático, tests y cobertura).

Para publicar una versión (ejemplo):

```sh
git tag v0.3.0 && git push origin main v0.3.0
```
