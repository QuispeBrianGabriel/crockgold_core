package com.crockgold.core.auth.domain.model.objects;

import com.crockgold.core.auth.domain.errors.InvalidPasswordError;
import com.crockgold.core.auth.domain.errors.InvalidPasswordError.Reason;
import java.util.regex.Pattern;

/**
 * Contraseña en claro que cumple la política de registro: entre 8 y 72 caracteres ASCII imprimibles
 * (letras, dígitos, símbolos y espacios), sin reglas de composición y sin alterar el valor. Al ser
 * ASCII, cada carácter ocupa un byte y el máximo coincide con los 72 bytes que compara BCrypt.
 */
public record Password(String value) {

  public static final String MASKED = "Password[****]";
  public static final int MIN_LENGTH = 8;
  public static final int MAX_LENGTH = 72;

  private static final Pattern PRINTABLE_ASCII = Pattern.compile("[\\x20-\\x7E]*");

  public Password {
    if (value == null || value.isBlank())
      throw new InvalidPasswordError(Reason.BLANK);

    if (!PRINTABLE_ASCII.matcher(value).matches())
      throw new InvalidPasswordError(Reason.INVALID_CHARACTERS);

    if (value.length() < MIN_LENGTH)
      throw new InvalidPasswordError(Reason.TOO_SHORT);

    if (value.length() > MAX_LENGTH)
      throw new InvalidPasswordError(Reason.TOO_LONG);
  }

  @Override
  public String toString() {
    return MASKED;
  }
}
