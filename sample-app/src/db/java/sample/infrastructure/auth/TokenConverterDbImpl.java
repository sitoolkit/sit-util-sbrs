package sample.infrastructure.auth;

import io.sitoolkit.util.sbrs.LoginUser;
import io.sitoolkit.util.sbrs.TokenConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TokenConverterDbImpl implements TokenConverter<LoginUser> {

  @Override
  public Map<String, String> toTokenExt(LoginUser principal) {
    Map<String, String> ext = new HashMap<>();

    ext.put("name", principal.getName());

    return ext;
  }

  @Override
  public LoginUser toPrincipal(String loginId, List<String> roles, Map<String, String> ext) {
    return new LoginUser(loginId, ext.get("name"), roles);
  }
}
