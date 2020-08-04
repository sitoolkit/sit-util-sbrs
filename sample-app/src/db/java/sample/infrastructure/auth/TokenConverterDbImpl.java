package sample.infrastructure.auth;

import io.sitoolkit.util.sbrs.LoginUser;
import io.sitoolkit.util.sbrs.TokenConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import sample.domain.user.UserEntity;

@Component
public class TokenConverterDbImpl implements TokenConverter<LoginUser<UserEntity>> {

  @Override
  public Map<String, String> toTokenExt(LoginUser<UserEntity> principal) {
    Map<String, String> ext = new HashMap<>();
    ext.put("name", principal.getEntity().getName());
    return ext;
  }

  @Override
  public LoginUser<UserEntity> toPrincipal(
      String loginId, List<String> roles, Map<String, String> ext) {
    UserEntity entity = new UserEntity();
    entity.setName(ext.get("name"));
    return new LoginUser<>(loginId, entity, roles);
  }
}
