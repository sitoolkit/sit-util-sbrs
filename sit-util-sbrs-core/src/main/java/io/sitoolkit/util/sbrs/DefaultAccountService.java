package io.sitoolkit.util.sbrs;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultAccountService<T extends LoginUser> implements UserDetailsService {

  protected AccountRepository<T> repository;

  protected PasswordEncoder encoder;

  public DefaultAccountService(AccountRepository<T> repository, PasswordEncoder encoder) {
    this.repository = repository;
    this.encoder = encoder;
  }

  @Override
  public UserDetails loadUserByUsername(String loginId) {
    return repository
        .findById(loginId)
        .orElseThrow(() -> new UsernameNotFoundException("Login Failed"));
  }
}
