# 0006 — API sin estado con HTTP Basic

- Estado: Aceptado
- Fecha: 2026-11-12

## Contexto

La API no debe quedar abierta, pero el estado de salud del servicio tiene que poder consultarse sin credenciales.

## Decisión

- Todas las peticiones requieren autenticación, salvo el endpoint de salud (`/actuator/health`).
- La autenticación es **HTTP Basic**: cada petición lleva sus credenciales.
- El servidor **no crea sesiones** ni cookies de autenticación.
- La protección **CSRF está desactivada**: sin sesión ni cookies no existe vector de ataque.

## Consecuencias

- Todo endpoint queda protegido por defecto, abrirlo requiere explicitarlo.
- La aplicación no arranca sin `SPRING_SECURITY_USER_PASSWORD`: no existe contraseña por defecto ni generada.
- El servidor no guarda nada entre una petición y la siguiente.
- Comportamiento cubierto por tests:
    - Salud pública.
    - `401` sin credenciales.
    - Ausencia de sesión y de cookie.
    - Fallo de arranque sin contraseña configurada.

## Dónde verlo

- `src/main/java/com/crockgold/core/SecurityConfiguration.java`
- `src/test/java/com/crockgold/core/SecurityConfigurationTests.java`
