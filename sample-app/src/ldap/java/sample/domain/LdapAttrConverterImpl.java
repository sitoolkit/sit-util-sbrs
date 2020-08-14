package sample.domain;

import io.sitoolkit.util.sbrs.LdapAttrConverter;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class LdapAttrConverterImpl implements LdapAttrConverter {
  public Map<String, String> convert(Map<String, String> args) {
    Map<String, String> conv = new HashMap<>();
    conv.put("cn", args.get("name"));
    conv.put("sn", args.get("name"));
    conv.put("givenName", "One");
    return conv;
  }
}
