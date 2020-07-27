package io.sitoolkit.util.sbrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.ldap.userdetails.PersonContextMapper;

public class AuthencicationManagerConfigurerLdapImpl implements AuthencicationManagerConfigurer {

  @Autowired LdapContextSource contextSource;

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.ldapAuthentication()
        .userDetailsContextMapper(new PersonContextMapper())
        .userDnPatterns("uid={0},ou=people")
        .groupSearchBase("ou=groups")
        //        .contextSource(contextSource);
        .contextSource()
        .url("ldap://localhost:389/dc=sitoolkit,dc=io");
  }
}
