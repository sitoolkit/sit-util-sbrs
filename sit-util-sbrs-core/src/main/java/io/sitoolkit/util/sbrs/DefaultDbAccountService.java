package io.sitoolkit.util.sbrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultDbAccountService<T extends AccountEntity> implements UserDetailsService {

  @Autowired AccountRepository<T> repository;

  @Autowired PasswordEncoder encoder;

  @Override
  public LoginUser<T> loadUserByUsername(String loginId) {
    T entity =
        repository
            .findById(loginId)
            .orElseThrow(() -> new UsernameNotFoundException("Login Failed"));

    return new LoginUser<>(
        entity.getId(), entity.getPassword(), entity, entity.getRoles().split(","));
  }
}
