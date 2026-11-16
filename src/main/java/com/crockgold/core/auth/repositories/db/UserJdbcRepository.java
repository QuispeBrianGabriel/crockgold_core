package com.crockgold.core.auth.repositories.db;

import com.crockgold.core.auth.repositories.entities.UserEntity;
import com.crockgold.core.auth.model.objects.Email;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserJdbcRepository extends CrudRepository<UserEntity, UUID> {

  boolean existsByEmail(Email email);

  Optional<UserEntity> findByEmail(Email email);
}
