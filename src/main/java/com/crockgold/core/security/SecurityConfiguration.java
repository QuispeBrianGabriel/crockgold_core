package com.crockgold.core.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.health.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.security.autoconfigure.SecurityProperties;
import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration(proxyBeanMethods = false)
class SecurityConfiguration {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeHttpRequests(
        auth -> auth.requestMatchers(EndpointRequest.to(HealthEndpoint.class))
          .permitAll()
          .anyRequest()
          .authenticated())
      .sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .csrf(csrf -> csrf.disable())
      .cors(withDefaults())
      .httpBasic(withDefaults())
      .build();
  }

  /**
   * El único origen de navegador admitido es el front, cuya URL llega en FRONT_URL. Sin ella no se
   * admite ninguno.
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource(@Value("${front.url:}") String frontUrl) {
    CorsConfiguration cors = new CorsConfiguration();
    cors.setAllowedOrigins(StringUtils.hasText(frontUrl) ? List.of(frontUrl.strip()) : List.of());
    cors.setAllowedMethods(List.of(CorsConfiguration.ALL));
    cors.setAllowedHeaders(List.of(CorsConfiguration.ALL));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cors);
    return source;
  }

  @Bean
  InMemoryUserDetailsManager apiUser(SecurityProperties properties) {
    SecurityProperties.User user = properties.getUser();
    if (user.isPasswordGenerated() || !StringUtils.hasText(user.getPassword())) {
      throw new IllegalStateException(
        "API credential missing: define SPRING_SECURITY_USER_PASSWORD");
    }
    return new InMemoryUserDetailsManager(User.withUsername(user.getName())
      .password("{noop}" + user.getPassword())
      .roles(user.getRoles().toArray(String[]::new))
      .build());
  }
}
