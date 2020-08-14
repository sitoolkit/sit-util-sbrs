package sample.domain.user;

import io.sitoolkit.util.sbrs.AccountEntityProvider;
import org.springframework.stereotype.Component;

@Component
public class UserEntityProvider implements AccountEntityProvider<UserEntity> {
  @Override
  public Class<UserEntity> getType() {
    return UserEntity.class;
  }
}
