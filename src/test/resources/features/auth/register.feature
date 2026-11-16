# Notación: ␣ es un espacio, [8×a] repite "a" 8 veces, [vacío] es un texto vacío y [nulo] un dato ausente.
# language: es
Característica: Registro de usuarios
  Como persona que quiere usar Crockgold
  Quiero registrarme con mi email y una contraseña
  Para tener una cuenta que solo yo pueda usar

  Regla: Un email y una contraseña válidos registran a un usuario, identificado por su email en minúsculas y sin espacios alrededor

    @happy
    Esquema del escenario: Se registra quien usa <caso>
      Cuando <email> se registra con la contraseña correct-horse-battery
      Entonces queda registrado como el usuario <usuario>

      Ejemplos:
        | caso                              | email                    | usuario                  |
        | un email y una contraseña válidos | john@example.com         | john@example.com         |
        | un email con mayúsculas           | John.Doe+tag@Example.COM | john.doe+tag@example.com |
        | un email con espacios alrededor   | ␣␣john@example.com␣      | john@example.com         |

  Regla: Un email pertenece a un solo usuario

    @unhappy
    Esquema del escenario: No se registra quien usa <caso>
      Dado que los siguientes usuarios están registrados:
        | email            | contraseña            |
        | john@example.com | correct-horse-battery |
      Cuando <email> se registra con la contraseña another-password
      Entonces se rechaza porque el email ya está registrado

      Ejemplos:
        | caso                                           | email              |
        | un email ya registrado                         | john@example.com   |
        | un email ya registrado, en otras mayúsculas    | JOHN@example.com   |
        | un email ya registrado, con espacios alrededor | ␣john@example.com␣ |

    @unhappy @border
    Escenario: Dos personas que se registran a la vez con el mismo email no lo comparten
      Cuando dos personas se registran a la vez con el email john@example.com
      Entonces solo una de ellas queda registrada

  Regla: El email debe ser una dirección de correo válida

    Esquema del escenario: Se registra un email <caso>
      Cuando <email> se registra con la contraseña correct-horse-battery
      Entonces queda registrado como el usuario <email>

      @happy
      Ejemplos: Formas admitidas
        | caso                                    | email                            |
        | con puntos y dígitos antes de la arroba | john.doe.123@example.com         |
        | con símbolos antes de la arroba         | !#$%&'*+/=?^_`{\|}~-@example.com |
        | con subdominios                         | john@mail.sub.example.com        |
        | con dígitos y guiones en el dominio     | john@my-company-123.com          |

      @happy @border
      Ejemplos: Límites admitidos
        | caso                                       | email                             |
        | de 254 caracteres                          | a@[63×b].[63×b].[63×b].[56×c].com |
        | con 64 caracteres antes de la arroba       | [64×a]@example.com                |
        | con 63 caracteres en una parte del dominio | john@[63×a].com                   |
        | con una terminación de 2 letras            | john@example.co                   |

    Esquema del escenario: No se registra un email <caso>
      Cuando <email> se registra con la contraseña correct-horse-battery
      Entonces se rechaza porque <motivo>

      @unhappy
      Ejemplos: Formas rechazadas
        | caso                                       | email                 | motivo                                     |
        | sin arroba                                 | plain                 | el email no tiene exactamente una arroba   |
        | con dos arrobas                            | a@b@example.com       | el email no tiene exactamente una arroba   |
        | sin nada antes de la arroba                | @example.com          | la parte anterior a la arroba no es válida |
        | que empieza por un punto                   | .john@example.com     | la parte anterior a la arroba no es válida |
        | con un punto justo antes de la arroba      | john.@example.com     | la parte anterior a la arroba no es válida |
        | con dos puntos seguidos antes de la arroba | john..doe@example.com | la parte anterior a la arroba no es válida |
        | con un espacio antes de la arroba          | jo␣hn@example.com     | la parte anterior a la arroba no es válida |
        | con letras acentuadas antes de la arroba   | jöhn@example.com      | la parte anterior a la arroba no es válida |
        | sin dominio                                | john@                 | el dominio del email no es válido          |
        | sin terminación en el dominio              | john@localhost        | el dominio del email no es válido          |
        | con dos puntos seguidos en el dominio      | john@example..com     | el dominio del email no es válido          |
        | con un dominio que empieza por guion       | john@-example.com     | el dominio del email no es válido          |
        | con una parte del dominio acabada en guion | john@example-.com     | el dominio del email no es válido          |
        | con un espacio en el dominio               | john@exa␣mple.com     | el dominio del email no es válido          |
        | con dígitos en la terminación              | john@example.c0m      | el dominio del email no es válido          |

      @unhappy @border
      Ejemplos: Límites rechazados
        | caso                                       | email                             | motivo                                           |
        | de 255 caracteres                          | a@[63×b].[63×b].[63×b].[57×c].com | el email es demasiado largo                      |
        | con 65 caracteres antes de la arroba       | [65×a]@example.com                | la parte anterior a la arroba es demasiado larga |
        | con 64 caracteres en una parte del dominio | john@[64×a].com                   | el dominio del email no es válido                |
        | con una terminación de 1 letra             | john@example.c                    | el dominio del email no es válido                |

  Regla: La contraseña debe tener al menos 8 caracteres y ocupar como máximo 72 bytes

    Esquema del escenario: Se registra una contraseña <caso>
      Cuando john@example.com se registra con la contraseña <contraseña>
      Entonces queda registrado como el usuario john@example.com

      @happy
      Ejemplos: Formas admitidas
        | caso                   | contraseña      |
        | de letras y dígitos    | MySecurePass123 |
        | con espacios           | ␣Pass␣word␣1␣   |
        | con espacios alrededor | ␣␣␣spaced␣␣␣    |

      @happy @border
      Ejemplos: Límites admitidos
        | caso                   | contraseña |
        | de 8 caracteres        |   12345678 |
        | de 8 emojis            | [8×🔑]      |
        | de 72 letras, 72 bytes | [72×a]     |
        | de 36 eñes, 72 bytes   | [36×ñ]     |
        | de 18 emojis, 72 bytes | [18×🔑]     |

    @unhappy @border
    Esquema del escenario: No se registra una contraseña <caso>
      Cuando john@example.com se registra con la contraseña <contraseña>
      Entonces se rechaza porque la contraseña es demasiado <defecto>

      Ejemplos:
        | caso                   | contraseña | defecto |
        | de 7 caracteres        |    1234567 | corta   |
        | de 7 emojis            | [7×🔑]      | corta   |
        | de 73 letras, 73 bytes | [73×a]     | larga   |
        | de 37 eñes, 74 bytes   | [37×ñ]     | larga   |
        | de 19 emojis, 76 bytes | [19×🔑]     | larga   |

  Regla: El email y la contraseña son obligatorios

    @unhappy
    Esquema del escenario: Nadie se registra con <caso>
      Cuando <email> se registra con la contraseña <contraseña>
      Entonces se rechaza porque faltan datos

      Ejemplos:
        | caso                            | email            | contraseña            |
        | el email ausente                | [nulo]           | correct-horse-battery |
        | el email vacío                  | [vacío]          | correct-horse-battery |
        | un email solo de espacios       | [3×␣]            | correct-horse-battery |
        | la contraseña ausente           | john@example.com | [nulo]                |
        | la contraseña vacía             | john@example.com | [vacío]               |
        | una contraseña solo de espacios | john@example.com | [8×␣]                 |
