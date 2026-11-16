package com.crockgold.core.auth.repositories;

import com.crockgold.core.auth.repositories.entities.UserEntity;
import com.crockgold.core.auth.domain.errors.EmailAlreadyRegisteredError;
import com.crockgold.core.auth.domain.model.User;
import com.crockgold.core.auth.domain.model.objects.Email;
import com.crockgold.core.auth.repositories.db.UserJdbcRepository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {

  private final UserJdbcRepository jdbcRepository;

  public boolean existsByEmail(Email email) {
    return jdbcRepository.existsByEmail(email);
  }

  public Optional<User> findByEmail(Email email) {
    return jdbcRepository.findByEmail(email).map(entity -> entity.toModel());
  }

  /** La restricción UNIQUE decide ante registros simultáneos del mismo email. */
  public User save(User user) {
    try {
      return jdbcRepository.save(UserEntity.from(user)).toModel();
    } catch (DuplicateKeyException exception) {
      throw new EmailAlreadyRegisteredError(user.getEmail());
    } catch (DbActionExecutionException exception) {
      if (exception.getCause() instanceof DuplicateKeyException)
        throw new EmailAlreadyRegisteredError(user.getEmail());
      throw exception;
    }
  }
}
