package com.crockgold.core.auth.services;

import com.crockgold.core.auth.domain.errors.EmailAlreadyRegisteredError;
import com.crockgold.core.auth.domain.errors.InvalidCredentialsError;
import com.crockgold.core.auth.domain.model.User;
import com.crockgold.core.auth.domain.model.objects.Email;
import com.crockgold.core.auth.domain.model.objects.Password;
import com.crockgold.core.auth.repositories.UserRepository;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  private final String rawPassword = "MySecret";
  private final String decoyHash;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.decoyHash = passwordEncoder.encode(rawPassword);
  }

  public User register(Email email, Password password) {
    if (userRepository.existsByEmail(email))
      throw new EmailAlreadyRegisteredError(email);

    String passwordHashed = passwordEncoder.encode(password.value());
    User user = User.create(email, passwordHashed);
    return userRepository.save(user);
  }

  public User login(Email email, String rawPassword) {
    Optional<User> user = userRepository.findByEmail(email);
    String hash = user.map(us -> us.getPasswordHash()).orElse(decoyHash);
    // BCrypt ignora lo que pasa de 72 bytes: sin esta comprobación, sobrarían caracteres al final.
    // Basta contar caracteres: uno que no sea ASCII ocupa más bytes, pero nunca coincide.
    boolean fits = rawPassword.length() <= Password.MAX_LENGTH;
    boolean matches = passwordEncoder.matches(rawPassword, hash) && fits;

    if (user.isEmpty() || !matches)
      throw new InvalidCredentialsError();

    return user.get();
  }
}
