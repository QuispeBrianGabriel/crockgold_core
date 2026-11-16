package com.crockgold.core.auth.model.objects;

import com.crockgold.core.auth.errors.InvalidPasswordError;
import com.crockgold.core.auth.errors.InvalidPasswordError.Reason;
import java.nio.charset.StandardCharsets;

/**
 * Contraseña en claro que cumple la política de registro (NIST SP 800-63B): longitud mínima, sin
 * reglas de composición y sin alterar el valor. El máximo son 72 bytes UTF-8, el límite de BCrypt.
 */
public record Password(String value) {

  public static final String MASKED = "Password[****]";
  public static final int MIN_LENGTH = 8;
  public static final int MAX_BYTES = 72;

  public Password {
    if (value == null || value.isBlank())
      throw new InvalidPasswordError(Reason.BLANK);

    if (value.codePointCount(0, value.length()) < MIN_LENGTH)
      throw new InvalidPasswordError(Reason.TOO_SHORT);

    if (value.getBytes(StandardCharsets.UTF_8).length > MAX_BYTES)
      throw new InvalidPasswordError(Reason.TOO_LONG);
  }

  @Override
  public String toString() {
    return MASKED;
  }
}
