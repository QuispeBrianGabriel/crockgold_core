package com.crockgold.core.auth.model.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.crockgold.core.auth.errors.InvalidEmailError;
import com.crockgold.core.auth.errors.InvalidEmailError.Reason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTests {

  @Test
  @DisplayName("Se normaliza a minúsculas y sin espacios laterales")
  void normalizesCaseAndSurroundingSpaces() {
    String email = new Email("  John.Doe+tag@Example.COM ").value();
    assertThat(email).isEqualTo("john.doe+tag@example.com");
  }

  @Test
  @DisplayName("Dos emails que solo difieren en mayúsculas o espacios son iguales")
  void emailsDifferingOnlyInCaseOrSurroundingSpacesAreEqual() {
    Email email1 = new Email(" John@Example.com\t");
    Email email2 = new Email("john@example.com");
    assertThat(email1).isEqualTo(email2);
  }

  @Test
  @DisplayName("La parte local admite letras, dígitos, puntos intermedios y los signos _ + - '")
  void localPartAllowsLettersDigitsInnerDotsAndSigns() {
    String email = new Email("john.o'doe_99-work+tag@example.com").value();
    assertThat(email).isEqualTo("john.o'doe_99-work+tag@example.com");
  }

  @Test
  @DisplayName("El dominio admite subdominios")
  void domainAllowsSubdomains() {
    String email = new Email("john@mail.sub.example.com").value();
    assertThat(email).isEqualTo("john@mail.sub.example.com");
  }

  @Test
  @DisplayName("Una etiqueta del dominio admite dígitos y guiones intermedios")
  void domainLabelAllowsDigitsAndInnerHyphens() {
    String email = new Email("john@my-company-123.com").value();
    assertThat(email).isEqualTo("john@my-company-123.com");
  }

  @ParameterizedTest
  @DisplayName("Rechaza un email nulo, vacío o solo con espacios")
  @NullAndEmptySource
  @ValueSource(strings = "   ")
  void rejectsBlankEmail(String blank) {
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email(blank))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.BLANK);
  }

  @ParameterizedTest
  @DisplayName("Rechaza un email sin arroba o con más de una")
  @ValueSource(strings = {"plain", "a@b@example.com"})
  void rejectsEmailWithoutExactlyOneAtSign(String value) {
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email(value))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.NOT_EXACTLY_ONE_AT_SIGN);
  }

  @Test
  @DisplayName("Rechaza una parte local vacía")
  void rejectsEmptyLocalPart() {
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email("@example.com"))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.INVALID_LOCAL_PART);
  }

  @ParameterizedTest
  @DisplayName("Rechaza un punto al inicio, al final o repetido en la parte local")
  @ValueSource(strings = {".john@example.com", "john.@example.com", "john..doe@example.com"})
  void rejectsMisplacedDotInLocalPart(String value) {
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email(value))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.INVALID_LOCAL_PART);
  }

  @ParameterizedTest
  @DisplayName("La parte local admite los signos _ + - ' al inicio, al final o seguidos")
  @ValueSource(strings = {
      "_john@example.com", "john-@example.com", "john+@example.com", "john__doe@example.com",
      "john.-doe@example.com", "'john@example.com"})
  void localPartAllowsSignsAnywhere(String value) {
    assertThat(new Email(value).value()).isEqualTo(value);
  }

  @ParameterizedTest
  @DisplayName("Rechaza en la parte local los símbolos de RFC 5322")
  @ValueSource(strings = {"jo!#$%&doe@exa.com", "jo*/=?^doe@exa.com", "jo`{|}~doe@exa.com"})
  void rejectsUncommonSymbolsInLocalPart(String value) {
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email(value))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.INVALID_LOCAL_PART);
  }

  @ParameterizedTest
  @DisplayName("Rechaza espacios o caracteres no ASCII en la parte local")
  @ValueSource(strings = {"jo hn@example.com", "jöhn@example.com"})
  void rejectsSpacesOrNonAsciiInLocalPart(String value) {
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email(value))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.INVALID_LOCAL_PART);
  }

  @ParameterizedTest
  @DisplayName("Rechaza un dominio vacío o sin dominio de primer nivel")
  @ValueSource(strings = {"john@", "john@localhost"})
  void rejectsDomainWithoutTopLevelDomain(String value) {
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email(value))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.INVALID_DOMAIN);
  }

  @ParameterizedTest
  @DisplayName("Rechaza una etiqueta del dominio vacía o que empieza o termina con guion")
  @ValueSource(strings = {"john@example..com", "john@-example.com", "john@example-.com"})
  void rejectsEmptyOrHyphenEdgedDomainLabel(String value) {
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email(value))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.INVALID_DOMAIN);
  }

  @Test
  @DisplayName("Rechaza espacios en el dominio")
  void rejectsSpacesInDomain() {
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email("john@exa mple.com"))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.INVALID_DOMAIN);
  }

  @Test
  @DisplayName("El dominio de primer nivel admite solo letras")
  void topLevelDomainAllowsOnlyLetters() {
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email("john@example.c0m"))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.INVALID_DOMAIN);
  }

  @Test
  @DisplayName("El dominio de primer nivel admite como minimo 2 letras")
  void topLevelDomainNeedsAtLeast2Letters() {
    String email = new Email("john@example.co").value();
    assertThat(email).isEqualTo("john@example.co");
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email("john@example.c"))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.INVALID_DOMAIN);
  }

  @Test
  @DisplayName("La parte local admite 64 caracteres y rechaza 65")
  void localPartAllowsUpTo64Characters() {
    String longest = "a".repeat(64) + "@example.com";
    String tooLong = "a".repeat(65) + "@example.com";

    assertThat(new Email(longest).value()).isEqualTo(longest);
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email(tooLong))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.LOCAL_TOO_LONG);
  }

  @Test
  @DisplayName("El email completo admite 254 caracteres y rechaza 255")
  void emailAllowsUpTo254Characters() {
    String start = "a@" + ("b".repeat(63) + ".").repeat(3);
    String longest = start + "c".repeat(56) + ".com";
    String tooLong = start + "c".repeat(57) + ".com";

    assertThat(new Email(longest).value()).hasSize(254);
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email(tooLong))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.TOO_LONG);
  }

  @Test
  @DisplayName("Cada etiqueta del dominio admite 63 caracteres y rechaza 64")
  void domainLabelAllowsUpTo63Characters() {
    String longest = "john@" + "a".repeat(63) + ".com";
    String tooLong = "john@" + "a".repeat(64) + ".com";

    assertThat(new Email(longest).value()).isEqualTo(longest);
    assertThatExceptionOfType(InvalidEmailError.class)
      .isThrownBy(() -> new Email(tooLong))
      .extracting(err -> err.reason())
      .isEqualTo(Reason.INVALID_DOMAIN);
  }
}
