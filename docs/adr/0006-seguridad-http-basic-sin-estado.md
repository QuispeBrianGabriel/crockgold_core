# 0006 — API sin estado con HTTP Basic

- Estado: Aceptado
- Fecha: 2026-11-12

## Contexto

La API no debe quedar abierta, pero su estado de salud tiene que poder consultarse sin credenciales.

## Decisión

- Todas las peticiones requieren autenticación, salvo `/actuator/health`.
- La autenticación es **HTTP Basic**: cada petición lleva sus credenciales.
- El servidor no crea sesiones ni cookies de autenticación.
- La protección CSRF está desactivada, porque sin sesión ni cookies no hay vector de ataque.

## Consecuencias

- Todo endpoint nuevo queda protegido por defecto. Abrirlo requiere declararlo.
- La aplicación no arranca sin `SPRING_SECURITY_USER_PASSWORD`: no hay contraseña por defecto ni generada.
- Los tests cubren la salud pública, el `401` sin credenciales, la ausencia de sesión y el fallo de arranque sin contraseña.

## Dónde verlo

`src/main/java/com/crockgold/core/SecurityConfiguration.java` y `src/test/java/com/crockgold/core/SecurityConfigurationTests.java`.
