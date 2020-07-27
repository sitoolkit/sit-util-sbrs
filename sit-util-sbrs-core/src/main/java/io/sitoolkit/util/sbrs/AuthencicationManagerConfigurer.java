package io.sitoolkit.util.sbrs;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

public interface AuthencicationManagerConfigurer {
  void configure(AuthenticationManagerBuilder auth) throws Exception;
}
