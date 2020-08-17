package io.sitoolkit.util.sbrs;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class DefaultDbAccountService<T extends AccountEntity>
    implements UserDetailsService, AccountService {

  @Autowired AccountRepository<T> repository;

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

  @SuppressWarnings({"unchecked"})
  @Override
  public boolean create(String loginId, String password, Map<String, String> ext) {
    if (repository.findById(loginId).isPresent()) {
      return false;
    }

    try {
      ResolvableType repositoryResolvableType =
          ResolvableType.forClass(repository.getClass()).as(AccountRepository.class);
      Class<?> entityClazz = repositoryResolvableType.getGeneric(0).resolve();

      ext.put("id", loginId);
      ext.put("password", encoder.encode(password));

      T account = modelMapper.map(ext, (Class<T>) entityClazz);
      repository.save(account);
      return true;

    } catch (Exception e) {
      log.error("exception occurred.", e);
      return false;
    }
  }
}
