package com.crockgold.core.auth.repositories;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.crockgold.core.auth.domain.errors.EmailAlreadyRegisteredError;
import com.crockgold.core.auth.domain.model.User;
import com.crockgold.core.auth.domain.model.objects.Email;
import com.crockgold.core.auth.repositories.db.UserJdbcRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

/**
 * Formas en que Spring Data puede entregar un fallo al guardar, difíciles de provocar a voluntad.
 */
class UserRepositoryTests {

  private static final Email EMAIL = new Email("john@example.com");
  private static final User USER = User.create(EMAIL, "hash");

  private final UserJdbcRepository jdbcRepo = mock(UserJdbcRepository.class);
  private final UserRepository repository = new UserRepository(jdbcRepo);

  @Test
  @DisplayName("Guardar un email duplicado falla como email ya registrado")
  void saveReportsDuplicateKeyAsAlreadyRegistered() {
    when(jdbcRepo.save(any())).thenThrow(new DuplicateKeyException("users_email_key"));

    assertThatExceptionOfType(EmailAlreadyRegisteredError.class)
      .isThrownBy(() -> repository.save(USER))
      .extracting(err -> err.email())
      .isEqualTo(EMAIL);
  }

  @Test
  @DisplayName("Guardar un email duplicado falla igual si Spring Data envuelve el error")
  void saveReportsWrappedDuplicateKeyAsAlreadyRegistered() {
    var wrapped = mock(DbActionExecutionException.class);
    when(wrapped.getCause()).thenReturn(new DuplicateKeyException("users_email_key"));
    when(jdbcRepo.save(any())).thenThrow(wrapped);

    assertThatExceptionOfType(EmailAlreadyRegisteredError.class)
      .isThrownBy(() -> repository.save(USER))
      .extracting(err -> err.email())
      .isEqualTo(EMAIL);
  }

  @Test
  @DisplayName("Guardar deja pasar cualquier otro fallo de la base sin disfrazarlo")
  void saveRethrowsOtherFailures() {
    var wrapped = mock(DbActionExecutionException.class);
    when(wrapped.getCause()).thenReturn(new QueryTimeoutException("timeout"));
    when(jdbcRepo.save(any())).thenThrow(wrapped);

    assertThatThrownBy(() -> repository.save(USER)).isSameAs(wrapped);
  }
}
