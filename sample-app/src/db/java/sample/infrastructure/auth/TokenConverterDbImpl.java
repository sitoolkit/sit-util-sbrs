package sample.infrastructure.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.sitoolkit.util.sbrs.TokenConverter;

@Component
public class TokenConverterDbImpl implements TokenConverter<SampleUser> {

  @Override
  public Map<String, String> toTokenExt(SampleUser principal) {
    Map<String, String> ext = new HashMap<>();

    ext.put("name", principal.getName());

    return ext;
  }

  @Override
  public SampleUser toPrincipal(String loginId, List<String> roles, Map<String, String> ext) {
    return new SampleUser(loginId, ext.get("name"), roles);
  }
}
