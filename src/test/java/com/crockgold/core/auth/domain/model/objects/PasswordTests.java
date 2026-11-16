package com.crockgold.core.auth.domain.model.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.crockgold.core.auth.domain.errors.InvalidPasswordError;
import com.crockgold.core.auth.domain.errors.InvalidPasswordError.Reason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTests {

  @ParameterizedTest
  @DisplayName("Conserva el valor sin alterarlo, incluidos los espacios")
  @ValueSource(strings = {" Pass word 1 ", "MySecurePass123", "   spaced   "})
  void keepsValueUntouched(String value) {
    String password = new Password(value).value();
    assertThat(password).isEqualTo(value);
  }

  @ParameterizedTest
  @DisplayName("Rechaza una contraseña nula, vacía o solo con espacios")
  @NullAndEmptySource
  @ValueSource(strings = "        ")
  void rejectsBlankPassword(String blank) {
    assertThatExceptionOfType(InvalidPasswordError.class)
      .isThrownBy(() -> new Password(blank))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.BLANK);
  }

  @Test
  @DisplayName("Admite como minimo 8 caracteres")
  void needsAtLeast8Characters() {
    String password = new Password("12345678").value();
    assertThat(password).isEqualTo("12345678");
    assertThatExceptionOfType(InvalidPasswordError.class)
      .isThrownBy(() -> new Password("1234567"))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.TOO_SHORT);
  }

  @Test
  @DisplayName("Admite 72 caracteres y rechaza 73, el límite de BCrypt")
  void allowsUpTo72Characters() {
    String longest = "a".repeat(72);
    String tooLong = "a".repeat(73);
    assertThat(new Password(longest).value()).isEqualTo(longest);
    assertThatExceptionOfType(InvalidPasswordError.class)
      .isThrownBy(() -> new Password(tooLong))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.TOO_LONG);
  }

  @Test
  @DisplayName("Admite todos los símbolos del teclado")
  void allowsEveryKeyboardSymbol() {
    String symbols = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    assertThat(new Password(symbols).value()).isEqualTo(symbols);
  }

  @ParameterizedTest
  @DisplayName("Rechaza letras acentuadas, eñes, emojis y caracteres de control")
  @ValueSource(strings = {"contraseña", "pásswörd", "🔑🔑🔑🔑🔑🔑🔑🔑", "pass\tword", "pass\nword"})
  void rejectsCharactersOutsidePrintableAscii(String value) {
    assertThatExceptionOfType(InvalidPasswordError.class)
      .isThrownBy(() -> new Password(value))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.INVALID_CHARACTERS);
  }

  @Test
  @DisplayName("toString no revela la contraseña")
  void toStringDoesNotLeakValue() {
    String text = "my-super-top-secret-password";
    String str = new Password(text).toString();
    assertThat(str).doesNotContain(text);
  }
}
