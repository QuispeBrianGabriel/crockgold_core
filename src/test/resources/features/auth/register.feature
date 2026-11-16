# language: es
Característica: Registro de usuarios
  Como persona con correo
  Quiero registrarme
  Para tener una cuenta

  Regla: Todos se registran con email y contraseña

    @happy
    Escenario: Se registra con un email y una contraseña válidos
      Cuando alguien se registra con cada uno de estos datos:
        | email                    | contraseña            |
        | john@example.com         | correct-horse-battery |
        | John.Doe+tag@Example.COM | correct-horse-battery |
        | ␣␣jane@example.com␣      | correct-horse-battery |
      Entonces queda registrado como cada uno de estos usuarios:
        | email                    |
        | john@example.com         |
        | john.doe+tag@example.com |
        | jane@example.com         |

  Regla: El email es obligatorio

    @unhappy
    Escenario: No se registra sin email o sin contraseña
      Cuando alguien se registra con cada uno de estos datos:
        | email            | contraseña            |
        | [nulo]           | correct-horse-battery |
        | [vacío]          | correct-horse-battery |
        | [3×␣]            | correct-horse-battery |
      Entonces se rechaza porque faltan datos

  Regla: La contraseña es obligatoria

    @unhappy
    Escenario: No se registra sin contraseña
      Cuando alguien se registra con cada uno de estos datos:
        | email            | contraseña |
        | john@example.com | [nulo]     |
        | john@example.com | [vacío]    |
        | john@example.com | [8×␣]      |
      Entonces se rechaza porque faltan datos

  Regla: Un email pertenece a un solo usuario

    @unhappy
    Escenario: No se registra con un email ya registrado
      Dado que los siguientes usuarios están registrados:
        | email            | contraseña            |
        | john@example.com | correct-horse-battery |
      Cuando alguien se registra con cada uno de estos datos:
        | email              | contraseña       |
        | john@example.com   | another-password |
        | JOHN@example.com   | another-password |
        | ␣john@example.com␣ | another-password |
      Entonces se rechaza porque el email ya está registrado

    @unhappy @border
    Escenario: Dos personas que se registran a la vez con el mismo email no lo comparten
      Cuando dos personas se registran a la vez con el email john@example.com
      Entonces solo una de ellas queda registrada

  Regla: El email tiene como máximo 254 caracteres

    @happy @border
    Escenario: Se registra con un email de 254 caracteres
      Cuando alguien se registra con cada uno de estos datos:
        | email                             | contraseña            |
        | a@[63×b].[63×b].[63×b].[56×c].com | correct-horse-battery |
      Entonces queda registrado con cada uno de esos emails

    @unhappy @border
    Escenario: No se registra con un email de 255 caracteres
      Cuando alguien se registra con cada uno de estos datos:
        | email                             | contraseña            |
        | a@[63×b].[63×b].[63×b].[57×c].com | correct-horse-battery |
      Entonces se rechaza porque el email es demasiado largo

  Regla: El email tiene exactamente una arroba, que separa el nombre del dominio

    @unhappy
    Escenario: No se registra con un email sin arroba o con más de una
      Cuando alguien se registra con cada uno de estos datos:
        | email           | contraseña            |
        | plain           | correct-horse-battery |
        | a@b@example.com | correct-horse-battery |
      Entonces se rechaza porque el email no tiene exactamente una arroba

  Regla: El nombre del email tiene entre 1 y 64 caracteres

    @happy @border
    Escenario: Se registra con un nombre del email de 64 caracteres
      Cuando alguien se registra con cada uno de estos datos:
        | email              | contraseña            |
        | [64×a]@example.com | correct-horse-battery |
      Entonces queda registrado con cada uno de esos emails

    @unhappy @border
    Escenario: No se registra con un nombre del email de 65 caracteres
      Cuando alguien se registra con cada uno de estos datos:
        | email              | contraseña            |
        | [65×a]@example.com | correct-horse-battery |
      Entonces se rechaza porque la parte anterior a la arroba es demasiado larga

    @unhappy @border
    Escenario: No se registra sin nombre del email
      Cuando alguien se registra con cada uno de estos datos:
        | email        | contraseña            |
        | @example.com | correct-horse-battery |
      Entonces se rechaza porque la parte anterior a la arroba no es válida

  Regla: El nombre del email puede tener letras sin acentos, dígitos y los signos _ + - ', con puntos sueltos solo entre ellos

    @happy
    Escenario: Se registra con un nombre del email de letras, dígitos, signos y puntos intermedios
      Cuando alguien se registra con cada uno de estos datos:
        | email                    | contraseña            |
        | john.doe.123@example.com | correct-horse-battery |
        | john_doe@example.com     | correct-horse-battery |
        | john-doe@example.com     | correct-horse-battery |
        | john+tag@example.com     | correct-horse-battery |
        | john.o'doe@example.com   | correct-horse-battery |
        | _john@example.com        | correct-horse-battery |
        | john-@example.com        | correct-horse-battery |
        | john__doe@example.com    | correct-horse-battery |
        | john.-doe@example.com    | correct-horse-battery |
      Entonces queda registrado con cada uno de esos emails

    @unhappy
    Escenario: No se registra con un nombre del email con puntos mal colocados
      Cuando alguien se registra con cada uno de estos datos:
        | email                 | contraseña            |
        | .john@example.com     | correct-horse-battery |
        | john.@example.com     | correct-horse-battery |
        | john..doe@example.com | correct-horse-battery |
      Entonces se rechaza porque la parte anterior a la arroba no es válida

    @unhappy
    Escenario: No se registra con un nombre del email con caracteres no admitidos
      Cuando alguien se registra con cada uno de estos datos:
        | email                 | contraseña            |
        | jo␣hn@example.com     | correct-horse-battery |
        | jöhn@example.com      | correct-horse-battery |
        | john!doe@example.com  | correct-horse-battery |
        | john#doe@example.com  | correct-horse-battery |
        | john$doe@example.com  | correct-horse-battery |
        | john%doe@example.com  | correct-horse-battery |
        | john&doe@example.com  | correct-horse-battery |
        | john*doe@example.com  | correct-horse-battery |
        | john/doe@example.com  | correct-horse-battery |
        | john=doe@example.com  | correct-horse-battery |
        | john?doe@example.com  | correct-horse-battery |
        | john^doe@example.com  | correct-horse-battery |
        | john`doe@example.com  | correct-horse-battery |
        | john{doe@example.com  | correct-horse-battery |
        | john\|doe@example.com | correct-horse-battery |
        | john}doe@example.com  | correct-horse-battery |
        | john~doe@example.com  | correct-horse-battery |
      Entonces se rechaza porque la parte anterior a la arroba no es válida

  Regla: El dominio se compone de partes separadas por puntos, cada una de letras sin acentos, dígitos y guiones intermedios

    @happy
    Escenario: Se registra con un dominio de varias partes, con dígitos y guiones intermedios
      Cuando alguien se registra con cada uno de estos datos:
        | email                     | contraseña            |
        | john@mail.sub.example.com | correct-horse-battery |
        | john@my-company-123.com   | correct-horse-battery |
      Entonces queda registrado con cada uno de esos emails

    @unhappy
    Escenario: No se registra con un dominio con partes vacías, guiones en los extremos o caracteres no admitidos
      Cuando alguien se registra con cada uno de estos datos:
        | email             | contraseña            |
        | john@             | correct-horse-battery |
        | john@example..com | correct-horse-battery |
        | john@-example.com | correct-horse-battery |
        | john@example-.com | correct-horse-battery |
        | john@exa␣mple.com | correct-horse-battery |
      Entonces se rechaza porque el dominio del email no es válido

  Regla: Cada parte del dominio tiene como máximo 63 caracteres

    @happy @border
    Escenario: Se registra con una parte del dominio de 63 caracteres
      Cuando alguien se registra con cada uno de estos datos:
        | email           | contraseña            |
        | john@[63×a].com | correct-horse-battery |
      Entonces queda registrado con cada uno de esos emails

    @unhappy @border
    Escenario: No se registra con una parte del dominio de 64 caracteres
      Cuando alguien se registra con cada uno de estos datos:
        | email           | contraseña            |
        | john@[64×a].com | correct-horse-battery |
      Entonces se rechaza porque el dominio del email no es válido

  Regla: El dominio acaba en una terminación de al menos 2 letras

    @happy @border
    Escenario: Se registra con una terminación de 2 letras
      Cuando alguien se registra con cada uno de estos datos:
        | email           | contraseña            |
        | john@example.co | correct-horse-battery |
      Entonces queda registrado con cada uno de esos emails

    @unhappy
    Escenario: No se registra con un dominio sin terminación o con una que no es solo de letras
      Cuando alguien se registra con cada uno de estos datos:
        | email            | contraseña            |
        | john@localhost   | correct-horse-battery |
        | john@example.c0m | correct-horse-battery |
      Entonces se rechaza porque el dominio del email no es válido

    @unhappy @border
    Escenario: No se registra con una terminación de 1 letra
      Cuando alguien se registra con cada uno de estos datos:
        | email          | contraseña            |
        | john@example.c | correct-horse-battery |
      Entonces se rechaza porque el dominio del email no es válido

  Regla: La contraseña tiene entre 8 y 72 caracteres

    @happy @border
    Escenario: Se registra con una contraseña en el límite de lo admitido
      Cuando alguien se registra con cada uno de estos datos:
        | email            | contraseña |
        | john@example.com | 12345678   |
        | jane@example.com | [72×a]     |
      Entonces queda registrado con cada uno de esos emails

    @unhappy @border
    Escenario: No se registra con una contraseña demasiado corta
      Cuando alguien se registra con cada uno de estos datos:
        | email            | contraseña |
        | john@example.com | 1234567    |
      Entonces se rechaza porque la contraseña es demasiado corta

    @unhappy @border
    Escenario: No se registra con una contraseña demasiado larga
      Cuando alguien se registra con cada uno de estos datos:
        | email            | contraseña |
        | john@example.com | [73×a]     |
      Entonces se rechaza porque la contraseña es demasiado larga

  Regla: La contraseña puede tener letras sin acentos, dígitos, símbolos del teclado y espacios

    @happy
    Escenario: Se registra con una contraseña de forma admitida
      Cuando alguien se registra con cada uno de estos datos:
        | email            | contraseña                          |
        | john@example.com | MySecurePass123                     |
        | jane@example.com | ␣Pass␣word␣1␣                       |
        | lucy@example.com | ␣␣␣spaced␣␣␣                        |
        | kate@example.com | !"#$%&'()*+,-./:;<=>?@[\\]^_`{\|}~ |
      Entonces queda registrado con cada uno de esos emails

    @unhappy
    Escenario: No se registra con una contraseña con caracteres no admitidos
      Cuando alguien se registra con cada uno de estos datos:
        | email            | contraseña     |
        | john@example.com | contrásena-123 |
        | john@example.com | contraseña-123 |
        | john@example.com | [8×🔑]         |
      Entonces se rechaza porque la contraseña tiene caracteres no admitidos
