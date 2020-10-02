package sample;

import java.util.UUID;
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
            .id(UUID.randomUUID().toString())
            .loginId("admin")
            .password(password)
            .name("Administrator")
            .mailAddress("admin@sample.com")
            .roles("ADMINS,USERS")
            .build());

    repo.save(
        UserEntity.builder()
            .id(UUID.randomUUID().toString())
            .loginId("user")
            .password(password)
            .name("User")
            .mailAddress("user@sample.com")
            .roles("USERS")
            .build());

    repo.save(
        UserEntity.builder()
            .id(UUID.randomUUID().toString())
            .loginId("changePw")
            .password(password)
            .name("ChangePassword")
            .mailAddress("changePw@sample.com")
            .roles("USERS")
            .build());
  }
}
