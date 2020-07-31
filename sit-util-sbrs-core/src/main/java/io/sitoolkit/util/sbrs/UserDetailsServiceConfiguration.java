package io.sitoolkit.util.sbrs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConditionalOnProperty(
    prefix = "sit.sbrs",
    name = "registory-type",
    havingValue = "db",
    matchIfMissing = true)
public class UserDetailsServiceConfiguration {

  @Bean
  @ConditionalOnMissingBean(UserDetailsService.class)
  public <T extends LoginUser> DefaultAccountService<T> accountService(
      AccountRepository<T> repository, PasswordEncoder encoder) {
    return new DefaultAccountService<>(repository, encoder);
  }
}
