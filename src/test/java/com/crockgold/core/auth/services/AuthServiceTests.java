package com.crockgold.core.auth.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.crockgold.core.auth.errors.EmailAlreadyRegisteredError;
import com.crockgold.core.auth.errors.InvalidCredentialsError;
import com.crockgold.core.auth.model.User;
import com.crockgold.core.auth.model.objects.Email;
import com.crockgold.core.auth.model.objects.Password;
import com.crockgold.core.auth.repositories.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthServiceTests {

  private static final Email EMAIL = new Email("john@example.com");
  private static final User STORED = new User(UUID.randomUUID(), EMAIL, "hash");

  private final UserRepository userRepo = mock(UserRepository.class);
  private final PasswordEncoder encoder = mock(PasswordEncoder.class);
  private final AuthService service = new AuthService(userRepo, encoder);

  @Nested
  class Register {

    @Test
    @DisplayName("Registrar un email ya registrado falla y no guarda nada")
    void registerRejectsAlreadyRegisteredEmail() {
      when(userRepo.existsByEmail(EMAIL)).thenReturn(true);

      assertThatExceptionOfType(EmailAlreadyRegisteredError.class)
        .isThrownBy(() -> service.register(EMAIL, new Password("correct-horse")))
        .extracting(err -> err.email())
        .isEqualTo(EMAIL);
      verify(userRepo, never()).save(any());
    }

    @Test
    @DisplayName("Registrar guarda el hash de la contraseña, nunca la contraseña")
    void registerStoresEncodedPasswordNotRawOne() {
      when(encoder.encode("correct-horse")).thenReturn("hash");

      service.register(EMAIL, new Password("correct-horse"));

      verify(userRepo).save(new User(null, EMAIL, "hash"));
    }

    @Test
    @DisplayName("Registrar devuelve el usuario tal como quedó guardado")
    void registerReturnsSavedUser() {
      when(userRepo.save(any())).thenReturn(STORED);

      User registered = service.register(EMAIL, new Password("correct-horse"));

      assertThat(registered).isEqualTo(STORED);
    }
  }

  @Nested
  class Login {

    @Test
    @DisplayName("Iniciar sesión con la contraseña correcta devuelve el usuario")
    void loginReturnsUserWhenPasswordMatches() {
      when(userRepo.findByEmail(EMAIL)).thenReturn(Optional.of(STORED));
      when(encoder.matches("correct-horse", "hash")).thenReturn(true);

      User loggedIn = service.login(EMAIL, "correct-horse");

      assertThat(loggedIn).isEqualTo(STORED);
    }

    @Test
    @DisplayName("Iniciar sesión con una contraseña incorrecta falla")
    void loginRejectsWrongPassword() {
      when(userRepo.findByEmail(EMAIL)).thenReturn(Optional.of(STORED));
      when(encoder.matches("wrong-password", "hash")).thenReturn(false);

      assertThatExceptionOfType(InvalidCredentialsError.class)
        .isThrownBy(() -> service.login(EMAIL, "wrong-password"));
    }

    @Test
    @DisplayName("Iniciar sesión con un email desconocido falla, pero compara la contraseña")
    void loginChecksPasswordEvenForUnknownEmail() {
      when(userRepo.findByEmail(EMAIL)).thenReturn(Optional.empty());

      assertThatExceptionOfType(InvalidCredentialsError.class)
        .isThrownBy(() -> service.login(EMAIL, "correct-horse"));
      verify(encoder).matches(eq("correct-horse"), any());
    }

    @Test
    @DisplayName("Iniciar sesión con más bytes de los que BCrypt compara falla")
    void loginRejectsPasswordLongerThanBcryptCompares() {
      String tooLong = "a".repeat(Password.MAX_BYTES + 1);
      when(userRepo.findByEmail(EMAIL)).thenReturn(Optional.of(STORED));
      when(encoder.matches(tooLong, "hash")).thenReturn(true);

      assertThatExceptionOfType(InvalidCredentialsError.class)
        .isThrownBy(() -> service.login(EMAIL, tooLong));
    }
  }
}
