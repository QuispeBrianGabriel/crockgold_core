package com.crockgold.core.auth.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
class AuthSecurityConfiguration {

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
    return http.securityMatcher("/auth/register", "/auth/login")
      .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
      .sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .csrf(csrf -> csrf.disable())
      .cors(withDefaults())
      .build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
