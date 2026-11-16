package com.crockgold.core.auth.config;

import com.crockgold.core.auth.model.objects.Email;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;

@Configuration(proxyBeanMethods = false)
class AuthPersistenceConfiguration {

  @Bean
  JdbcCustomConversions authJdbcCustomConversions() {
    return new JdbcCustomConversions(List.of(new EmailToString(), new StringToEmail()));
  }

  @WritingConverter
  static class EmailToString implements Converter<Email, String> {
    @Override
    public String convert(Email source) {
      return source.value();
    }
  }

  @ReadingConverter
  static class StringToEmail implements Converter<String, Email> {
    @Override
    public Email convert(String source) {
      return new Email(source);
    }
  }
}
