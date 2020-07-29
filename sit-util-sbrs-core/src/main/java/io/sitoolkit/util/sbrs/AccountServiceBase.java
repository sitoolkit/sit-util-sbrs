package io.sitoolkit.util.sbrs;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public abstract class AccountServiceBase<T extends AccountEntityBase>
    implements UserDetailsService {

  protected AccountRepositoryBase<T> repository;

  protected PasswordEncoder encoder;

  public AccountServiceBase(AccountRepositoryBase<T> repository, PasswordEncoder encoder) {
    this.repository = repository;
    this.encoder = encoder;
  }

  @Override
  public UserDetails loadUserByUsername(String loginId) {

    AccountEntityBase entity =
        repository
            .findById(loginId)
            .orElseThrow(() -> new UsernameNotFoundException("Login Failed"));

    LoginUser user =
        new LoginUser(
            entity.getId(), entity.getPassword(), entity.getName(), entity.getRoles().split(","));

    user.setName(entity.getName());

    return user;
  }
}
