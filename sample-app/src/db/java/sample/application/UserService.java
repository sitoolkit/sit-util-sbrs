package sample.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import sample.domain.user.UserEntity;
import sample.domain.user.UserRepository;
import sample.infrastructure.auth.SampleUser;

@Component
public class UserService implements UserDetailsService {

  @Autowired UserRepository repository;

  @Autowired PasswordEncoder encoder;

  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

    UserEntity entity =
        repository
            .findById(loginId)
            .orElseThrow(() -> new UsernameNotFoundException("Login Failed"));

    SampleUser user =
        new SampleUser(
            entity.getId(), entity.getPassword(), entity.getName(), entity.getRoles().split(","));

    user.setName(entity.getName());

    return user;
  }
}
