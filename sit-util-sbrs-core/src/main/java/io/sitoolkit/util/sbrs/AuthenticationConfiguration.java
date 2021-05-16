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
  private AccountService accountService;

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
  public AccountService accountService() {
    if (Objects.isNull(this.accountService)) {
      initializeUserDetailsService();
    }
    return this.accountService;
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
        this.accountService = (AccountService) this.userDetailsService;
        break;
      case "ldap":
        this.userDetailsService = null;
        this.accountService = null;
        break;
      default:
        this.userDetailsService = new DefaultDbAccountService<>();
        this.accountService = (AccountService) this.userDetailsService;
    }
  }
}
