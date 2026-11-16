# 0012 — Arquitectura hexagonal no estricta

- Estado: Aceptado
- Fecha: 2026-11-13

## Contexto

La hexagonal estricta obliga a puertos, adaptadores y mapeadores para cada dependencia. Menos código es menos bugs.

## Decisión

- Cada módulo es un paquete de primer nivel, `com.crockgold.core.<modulo>`, preparado para Spring Modulith.
- Sus carpetas son `config`, `controllers`, `domain`, `dto`, `repositories` y `services`. Una carpeta solo contiene un tipo de clase.
- El dominio vive en `domain/`: el modelo en `domain/model/` y los errores en `domain/errors/`.
- Los value objects van en `domain/model/objects/`.
- La persistencia vive dentro de `repositories/`: las entidades en `entities/` y las interfaces de Spring Data JDBC en `db/`.
- `domain/` es Java puro. La única dependencia permitida es Lombok.
- La entidad (`UserEntity`) es distinta del modelo (`User`) y sabe convertirse en él.
- El repositorio (`UserRepository`) recibe y devuelve solo clases del modelo. Internamente usa una interfaz de Spring Data JDBC sobre la entidad.
- Los errores de dominio son excepciones con sufijo `Error` (`InvalidCredentialsError`), sin nombres de tecnología.
- Cada módulo declara su propia configuración, incluida su `SecurityFilterChain`. La global vive en el módulo `security`.
- Las dependencias se inyectan por constructor.

## Consecuencias

- Los servicios se prueban con mocks del repositorio y del `PasswordEncoder`, sin base de datos.
- Los repositorios convierten entre entidad y modelo. Los servicios solo ven el modelo.
- Cambiar de framework afecta a servicios, repositorios y controladores, no al modelo.

## Dónde verlo

`src/main/java/com/crockgold/core/auth/` y `src/main/java/com/crockgold/core/security/`.
