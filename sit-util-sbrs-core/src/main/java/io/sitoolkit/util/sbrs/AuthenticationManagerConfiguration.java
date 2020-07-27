package io.sitoolkit.util.sbrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationManagerConfiguration {

  @Autowired SbrsProperties securityProperties;

  @Bean
  public AuthencicationManagerConfigurer authencicationManagerConfigurer() {
    String type = securityProperties.getRegistoryType().toLowerCase();

    switch (type) {
      case "db":
        return new AuthencicationManagerConfigurerDbImpl();
      case "ldap":
        return new AuthencicationManagerConfigurerLdapImpl();
      default:
        return new AuthencicationManagerConfigurerDbImpl();
    }
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
