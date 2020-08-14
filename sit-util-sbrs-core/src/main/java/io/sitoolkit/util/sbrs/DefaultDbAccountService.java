package io.sitoolkit.util.sbrs;

import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultDbAccountService<T extends AccountEntity>
    implements UserDetailsService, DefaultAccountService {

  @Autowired AccountRepository<T> repository;

  @Autowired AccountEntityProvider<T> provider;

  @Autowired PasswordEncoder encoder;

  @Autowired ModelMapper modelMapper;

  @Override
  public LoginUser<T> loadUserByUsername(String loginId) {
    T entity =
        repository
            .findById(loginId)
            .orElseThrow(() -> new UsernameNotFoundException("Login Failed"));

    return new LoginUser<>(
        entity.getId(), entity.getPassword(), entity, entity.getRoles().split(","));
  }

  @Override
  public boolean create(String loginId, String password, Map<String, String> ext) {
    if (repository.findById(loginId).isPresent()) {
      return false;
    }

    ext.put("id", loginId);
    ext.put("password", encoder.encode(password));
    T account = modelMapper.map(ext, provider.getType());
    repository.save(account);
    return true;
  }
}
