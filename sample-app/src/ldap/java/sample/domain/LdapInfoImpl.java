package sample.domain;

import io.sitoolkit.util.sbrs.LdapInfo;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class LdapInfoImpl implements LdapInfo {
  private String accountBaseDn = "ou=people,dc=sitoolkit,dc=io";
  private String accountIdentifierElement = "uid";

  private String groupBaseDn = "ou=groups,dc=sitoolkit,dc=io";
  private String groupIdentifierElement = "cn";

  private String[] objectClasses = new String[] {"top", "person", "inetOrgPerson"};
}
