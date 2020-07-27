package sample;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sample.domain.user.UserEntity;
import sample.domain.user.UserRepository;

@Component
public class DataGenerator {

  @Autowired UserRepository repo;

  @Autowired PasswordEncoder encoder;

  @PostConstruct
  @Transactional
  public void generate() {
    String password = encoder.encode("password");
    repo.save(
        UserEntity.builder()
            .id("admin")
            .password(password)
            .name("Administrator")
            .roles("ADMINS,USERS")
            .build());

    repo.save(
        UserEntity.builder().id("user").password(password).name("User").roles("USER").build());
  }
}
