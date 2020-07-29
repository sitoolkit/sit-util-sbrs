package sample.application;

import io.sitoolkit.util.sbrs.AccountServiceBase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sample.domain.user.UserEntity;
import sample.domain.user.UserRepository;

@Service
public class UserService extends AccountServiceBase<UserEntity> {
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    super(userRepository, passwordEncoder);
  }
}
