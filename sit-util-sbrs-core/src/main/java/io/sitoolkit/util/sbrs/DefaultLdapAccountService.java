package io.sitoolkit.util.sbrs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.naming.ldap.LdapName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;

@Slf4j
public class DefaultLdapAccountService implements DefaultAccountService {

  @Autowired LdapTemplate ldapTemplate;

  @Autowired LdapInfo ldapInfo;

  @Autowired LdapAttrConverter ldapAttrConverter;

  @Override
  public boolean create(String loginId, String password, Map<String, String> ext) {
    try {
      String createAccount = createAccount(loginId, password, ext);
      if (ext.containsKey("roles")) {
        addGroup(createAccount, Arrays.asList(ext.get("roles").split(",")));
      }
      return true;

    } catch (RuntimeException e) {
      log.error("account create feilure.", e);
      return false;
    }
  }

  private String createAccount(String loginId, String password, Map<String, String> ext) {
    LdapName dn =
        LdapNameBuilder.newInstance()
            .add(ldapInfo.getAccountBaseDn())
            .add(ldapInfo.getAccountIdentifierElement(), loginId)
            .build();
    DirContextAdapter context = new DirContextAdapter(dn);

    context.setAttributeValues("objectclass", ldapInfo.getObjectClasses());
    context.setAttributeValue("userPassword", password);
    ldapAttrConverter
        .convert(ext)
        .entrySet()
        .forEach(entry -> context.setAttributeValue(entry.getKey(), entry.getValue()));
    ldapTemplate.bind(context);

    return dn.toString();
  }

  private void addGroup(String createAccount, List<String> roles) {
    for (String role : roles) {
      LdapName dn =
          LdapNameBuilder.newInstance()
              .add(ldapInfo.getGroupBaseDn())
              .add(ldapInfo.getGroupIdentifierElement(), StringUtils.lowerCase(role))
              .build();
      DirContextOperations context = ldapTemplate.lookupContext(dn);

      context.addAttributeValue("uniqueMember", createAccount);
      ldapTemplate.modifyAttributes(context);
    }
  }
}
