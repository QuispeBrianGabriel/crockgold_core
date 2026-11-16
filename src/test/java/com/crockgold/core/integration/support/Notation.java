package com.crockgold.core.integration.support;

import java.util.regex.Pattern;

/**
 * Notación con la que los escenarios escriben datos que una tabla no deja ver: {@code ␣} es un
 * espacio, {@code [8×a]} repite "a" 8 veces, {@code [vacío]} es un texto vacío y {@code [nulo]} un
 * dato ausente.
 */
public final class Notation {

  private static final Pattern REPETITION = Pattern.compile("\\[(\\d+)×([^\\]]+)]");

  private Notation() {}

  public static String expand(String text) {
    if ("[nulo]".equals(text))
      return null;
    if ("[vacío]".equals(text))
      return "";
    return REPETITION.matcher(text)
      .replaceAll(match -> match.group(2).repeat(Integer.parseInt(match.group(1))))
      .replace('␣', ' ');
  }
}
