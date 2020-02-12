package sample.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.ldap.userdetails.Person;
import org.springframework.security.ldap.userdetails.Person.Essence;
import org.springframework.stereotype.Component;

import io.sitoolkit.util.sbrs.SpringSecurityUtils;
import io.sitoolkit.util.sbrs.TokenConverter;

@Component
public class PrincipalExtractorLdapImpl implements TokenConverter<Person> {

  @Override
  public Map<String, String> toTokenExt(Person principal) {
    Map<String, String> ext = new HashMap<>();
    ext.put("name", principal.getCn()[0]);
    return ext;
  }

  @Override
  public Person toPrincipal(String loginId, List<String> roles, Map<String, String> ext) {
    Essence essence = new Person.Essence();
    essence.setUsername(loginId);
    essence.setAuthorities(SpringSecurityUtils.toAuthrities(roles));
    essence.setCn(new String[] {ext.get("name")});
    essence.setDn("DoNotUse");
    return (Person) essence.createUserDetails();
  }
}
