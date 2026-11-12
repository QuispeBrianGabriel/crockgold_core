package com.crockgold.core;

import static org.springframework.security.config.Customizer.withDefaults;

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

@Configuration(proxyBeanMethods = false)
class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth.requestMatchers(EndpointRequest.to(HealthEndpoint.class))
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    InMemoryUserDetailsManager apiUser(SecurityProperties properties) {
        SecurityProperties.User user = properties.getUser();
        if (user.isPasswordGenerated() || !StringUtils.hasText(user.getPassword())) {
            throw new IllegalStateException("API credential missing: define SPRING_SECURITY_USER_PASSWORD");
        }
        return new InMemoryUserDetailsManager(User.withUsername(user.getName())
                .password("{noop}" + user.getPassword())
                .roles(user.getRoles().toArray(String[]::new))
                .build());
    }
}
