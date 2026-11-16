# Notación: ␣ es un espacio, [8×a] repite "a" 8 veces, [vacío] es un texto vacío y [nulo] un dato ausente.
# language: es
Característica: Inicio de sesión
  Como usuario registrado
  Quiero iniciar sesión
  Para ingresar al sistema

  Antecedentes:
    Dado que los siguientes usuarios están registrados:
      | email            | contraseña            |
      | john@example.com | correct-horse-battery |
      | jane@example.com | ␣Pass␣word␣1␣         |
      | kate@example.com | [8×🔑]                 |
      | lucy@example.com | [72×a]                |

  Regla: El email y la contraseña deben coincidir con un registro existente

    Esquema del escenario: Inicia sesión quien usa <caso>
      Cuando <email> inicia sesión con la contraseña <contraseña>
      Entonces se le reconoce como el usuario <usuario>

      @happy
      Ejemplos:
        | caso                            | email              | contraseña            | usuario          |
        | su email y su contraseña        | john@example.com   | correct-horse-battery | john@example.com |
        | su email con otras mayúsculas   | JOHN@Example.com   | correct-horse-battery | john@example.com |
        | su email con espacios alrededor | ␣john@example.com␣ | correct-horse-battery | john@example.com |
        | su contraseña con espacios      | jane@example.com   | ␣Pass␣word␣1␣         | jane@example.com |
        | su contraseña de emojis         | kate@example.com   | [8×🔑]                 | kate@example.com |

      @happy @border
      Ejemplos: Límites
        | caso                      | email            | contraseña | usuario          |
        | su contraseña de 72 bytes | lucy@example.com | [72×a]     | lucy@example.com |

  Regla: Un intento fallido no revela si falló el email o la contraseña

    Esquema del escenario: No inicia sesión quien usa <caso>
      Cuando <email> inicia sesión con la contraseña <contraseña>
      Entonces se rechaza porque las credenciales son incorrectas

      @unhappy
      Ejemplos:
        | caso                                        | email              | contraseña            |
        | una contraseña equivocada                   | john@example.com   | wrong-password        |
        | su contraseña con otras mayúsculas          | john@example.com   | Correct-Horse-Battery |
        | su contraseña sin los espacios de alrededor | jane@example.com   | Pass␣word␣1           |
        | la contraseña de otro usuario               | john@example.com   | ␣Pass␣word␣1␣         |
        | un email sin registrar                      | nobody@example.com | correct-horse-battery |

      @unhappy @border
      Ejemplos: Límites
        | caso                                     | email            | contraseña |
        | su contraseña de 72 bytes y uno de más   | lucy@example.com | [73×a]     |
        | su contraseña de 72 bytes y uno de menos | lucy@example.com | [71×a]     |

  Regla: El email y la contraseña son obligatorios, y el email debe ser una dirección válida

    @unhappy
    Esquema del escenario: No inicia sesión quien lo intenta con <caso>
      Cuando <email> inicia sesión con la contraseña <contraseña>
      Entonces se rechaza porque <motivo>

      Ejemplos:
        | caso                                          | email                             | contraseña            | motivo                                           |
        | el email ausente                              | [nulo]                            | correct-horse-battery | faltan datos                                     |
        | el email vacío                                | [vacío]                           | correct-horse-battery | faltan datos                                     |
        | un email solo de espacios                     | [3×␣]                             | correct-horse-battery | faltan datos                                     |
        | la contraseña ausente                         | john@example.com                  | [nulo]                | faltan datos                                     |
        | la contraseña vacía                           | john@example.com                  | [vacío]               | faltan datos                                     |
        | una contraseña solo de espacios               | john@example.com                  | [8×␣]                 | faltan datos                                     |
        | un email sin arroba                           | plain                             | correct-horse-battery | el email no tiene exactamente una arroba         |
        | un email que empieza por un punto             | .john@example.com                 | correct-horse-battery | la parte anterior a la arroba no es válida       |
        | un email sin terminación en el dominio        | john@localhost                    | correct-horse-battery | el dominio del email no es válido                |
        | un email de 255 caracteres                    | a@[63×b].[63×b].[63×b].[57×c].com | correct-horse-battery | el email es demasiado largo                      |
        | un email con 65 caracteres antes de la arroba | [65×a]@example.com                | correct-horse-battery | la parte anterior a la arroba es demasiado larga |
