package com.crockgold.core.auth.model.objects;

import com.crockgold.core.auth.errors.InvalidEmailError.Reason;
import com.crockgold.core.auth.errors.InvalidEmailError;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Dirección de correo según RFC 5321 (límites) y RFC 5322 (dot-atom), restringida a ASCII. Se
 * normaliza (sin espacios laterales y en minúsculas) para que la identidad sea estable.
 */
public record Email(String value) {

  public static final int MAX_LENGTH = 254;
  public static final int MAX_LOCAL_LENGTH = 64;

  private static final String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+";
  private static final String LABEL = "[a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?";
  private static final String TLD = "[a-z]{2,63}";

  private static final Pattern LOCAL_PART = Pattern.compile(ATOM + "(\\." + ATOM + ")*");
  private static final Pattern DOMAIN = Pattern.compile("(" + LABEL + "\\.)+" + TLD);

  public Email {
    if (value == null || value.isBlank())
      throw new InvalidEmailError(Reason.BLANK);

    value = value.strip().toLowerCase(Locale.ROOT);

    if (value.length() > MAX_LENGTH)
      throw new InvalidEmailError(Reason.TOO_LONG);

    int at = value.indexOf('@');
    if (at < 0 || at != value.lastIndexOf('@'))
      throw new InvalidEmailError(Reason.NOT_EXACTLY_ONE_AT_SIGN);

    String local = value.substring(0, at);
    String domain = value.substring(at + 1);

    if (local.length() > MAX_LOCAL_LENGTH)
      throw new InvalidEmailError(Reason.LOCAL_TOO_LONG);

    if (!LOCAL_PART.matcher(local).matches())
      throw new InvalidEmailError(Reason.INVALID_LOCAL_PART);

    if (!DOMAIN.matcher(domain).matches())
      throw new InvalidEmailError(Reason.INVALID_DOMAIN);
  }
}
