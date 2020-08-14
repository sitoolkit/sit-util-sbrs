package io.sitoolkit.util.sbrs;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationConfiguration {

  @Autowired private SbrsProperties securityProperties;

  private UserDetailsService userDetailsService;
  private DefaultAccountService defaultAccountService;

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

  @Bean
  @ConditionalOnMissingBean(UserDetailsService.class)
  public UserDetailsService userDetailsService() {
    String type = securityProperties.getRegistoryType().toLowerCase();
    if (StringUtils.equals("db", type) && Objects.isNull(this.userDetailsService)) {
      initializeUserDetailsService();
    }
    return this.userDetailsService;
  }

  @Bean
  public DefaultAccountService defaultAccountService() {
    if (Objects.isNull(this.defaultAccountService)) {
      initializeUserDetailsService();
    }
    return this.defaultAccountService;
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  private void initializeUserDetailsService() {
    String type = securityProperties.getRegistoryType().toLowerCase();

    switch (type) {
      case "db":
        this.userDetailsService = new DefaultDbAccountService<>();
        this.defaultAccountService = (DefaultAccountService) this.userDetailsService;
        break;
      case "ldap":
        this.userDetailsService = null;
        this.defaultAccountService = new DefaultLdapAccountService();
        break;
      default:
        this.userDetailsService = new DefaultDbAccountService<>();
        this.defaultAccountService = (DefaultAccountService) this.userDetailsService;
    }
  }
}
